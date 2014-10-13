package org.janelia.img.pyramid;

import java.util.ArrayList;
import java.util.List;

import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

public class GaussianPyramid< T extends RealType< T > > extends AbstractPyramid< T > {
	
	public GaussianPyramid( final RandomAccessibleInterval< T > source, 
			final double scalingFactor,
			final double sigma, 
			final ImgFactory< T > factory ) {
		super( sourceToListOfScales(source, scalingFactor, sigma, factory, source.randomAccess().get() ),
				factory );
	}
	
	public static <U extends RealType< U > > List< RandomAccessibleInterval< U > > sourceToListOfScales( final RandomAccessibleInterval< U > source, 
			final double scalingFactor, 
			final double sigma, 
			final ImgFactory< U > factory, 
			final U type ) {
		
		final ArrayList<RandomAccessibleInterval<U>> images = new ArrayList< RandomAccessibleInterval< U > >();
		images.add( source );
		
		RandomAccessibleInterval<U> previous = source;
		long shortDimension = Math.min( previous.dimension( 0 ), previous.dimension( 1 ) );
		
		while ( shortDimension > 1 ) {
			
			final FinalInterval dim = new FinalInterval( (long) ( previous.dimension( 0 )*scalingFactor ), (long)( previous.dimension( 1 )*scalingFactor ) );
			final RandomAccessibleInterval<U> scaled = factory.create( dim, type );
			
			try {
				Gauss3.gauss( sigma, Views.extendMirrorSingle( previous ), scaled );
			} catch (final IncompatibleTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			images.add( scaled );
			previous = scaled;
			shortDimension = Math.min( previous.dimension( 0 ), previous.dimension( 1 ) );
		}
		
		return images;
	}
	
	

}