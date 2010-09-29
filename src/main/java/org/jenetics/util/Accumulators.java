/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 * 	 Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 * 	 
 */
package org.jenetics.util;

import javolution.context.ConcurrentContext;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public final class Accumulators {

	private Accumulators() {
		throw new AssertionError("Don't create an 'Accumulators' instance.");
	}
	
	/**
	 * <p>Calculate  the Arithmetic mean:</p>
	 * <p><img src="doc-files/arithmetic-mean.gif" alt="Arithmentic Mean" /></p>
	 * 
	 * @see <a href="http://mathworld.wolfram.com/ArithmeticMean.html">Wolfram MathWorld: Artithmetic Mean</a>
	 * @see <a href="http://en.wikipedia.org/wiki/Arithmetic_mean">Wikipedia: Arithmetic Mean</a>
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	public static class Mean<N extends Number> extends AdaptableAccumulator<N> {

		protected double _mean = Double.NaN;
		
		public Mean() {
		}
		
		/**
		 * Return the mean value of the accumulated values.
		 * 
		 * @return the mean value of the accumulated values, or {@link java.lang.Double#NaN}
		 * 		  if {@code getSamples() == 0}.
		 */
		public double getMean() {
			return _mean;
		}
		
		public double getStandardError() {
			double sem = Double.NaN;

			if (_samples > 0) {
				sem = _mean/Math.sqrt(_samples);
			}
			
			return sem;
		}
		
		/**
		 * @throws NullPointerException if the given {@code value} is {@code null}.
		 */
		@Override
		public void accumulate(final N value) {
			if (_samples == 0) {
				_mean = 0;
			}
			++_samples;
			
			_mean += (value.doubleValue() - _mean)/(double)_samples;
		}
		
		@Override
		public String toString() {
			return String.format(
						"%s[samples=%d, mean=%f, stderr=%f]", 
						getClass().getSimpleName(), 
						getSamples(), 
						getMean(), 
						getStandardError()
					);
		}
	}
	
	
	/**
	 * <p>Calculate the variance from a finite sample of <i>n</i> observations:</p>
	 * <p><img src="doc-files/variance.gif" alt="Variance" /></p>
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance" >
	 * 		  Wikipedia: Algorithms for calculating variance</a>
	 * @see <a href="http://mathworld.wolfram.com/Variance.html">
	 * 		  Wolfram MathWorld: Variance</a>
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	public static class Variance<N extends Number> extends Mean<N> {
		
		private double _m2 = Double.NaN;
		
		public Variance() {
		}
		
		/**
		 * Return the variance of the accumulated values.
		 * <p><img src="doc-files/variance.gif" alt="Variance" /></p>
		 * 
		 * @return the variance of the accumulated values, or {@link java.lang.Double#NaN}
		 *         if {@code getSamples() == 0}.
		 */
		public double getVariance() {
			double variance = Double.NaN;
			
			if (_samples == 1) {
				variance = _m2;
			} else if (_samples > 1) {
				variance = _m2/(double)(_samples - 1);
			}
			
			return variance;
		}
		
		/**
		 * @throws NullPointerException if the given {@code value} is {@code null}.
		 */
		@Override
		public void accumulate(final N value) {
			if (_samples == 0) {
				_mean = 0;
				_m2 = 0;
			}
			++_samples;
			
			final double data = value.doubleValue();
			final double delta = data - _mean;
			_mean += delta/(double)_samples;
			_m2 += delta*(data - _mean);
		}
		
		@Override
		public String toString() {
			return String.format(
						"%s[samples=%d, mean=%f, stderr=%f, var=%f]", 
						getClass().getSimpleName(), 
						getSamples(), 
						getMean(), 
						getStandardError(), 
						getVariance()
					);
		}
	}

	/**
	 * Calculates min value.
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	public static class Min<C extends Comparable<? super C>> 
		extends AdaptableAccumulator<C> 
	{
		private C _min;
		
		public Min() {
		}
		
		public C getMin() {
			return _min;
		}
		
		/**
		 * @throws NullPointerException if the given {@code value} is {@code null}.
		 */
		@Override
		public void accumulate(final C value) {
			if (_min == null) {
				_min = value;
			} else {
				if (value.compareTo(_min) < 0) {
					_min = value;
				}
			}
			
			++_samples;
		}
		
		@Override
		public String toString() {
			return String.format(
					"%s[samples=%d, min=%s]", 
					getClass().getSimpleName(), getSamples(), getMin()
				);
		}
	}
	
	
	/**
	 * Calculates max value.
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	public static class Max<C extends Comparable<? super C>> 
		extends AdaptableAccumulator<C> 
	{
		private C _max;
		
		public Max() {
		}
		
		public C getMax() {
			return _max;
		}
		
		/**
		 * @throws NullPointerException if the given {@code value} is {@code null}.
		 */
		@Override
		public void accumulate(final C value) {
			if (_max == null) {
				_max = value;
			} else {
				if (value.compareTo(_max) > 0) {
					_max = value;
				}
			}
			
			++_samples;
		}
		
		@Override
		public String toString() {
			return String.format(
					"%s[samples=%d, max=%s]", 
					getClass().getSimpleName(), getSamples(), getMax()
				);
		}
	}
	
	
	/**
	 * Calculates min and max values.
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	public static class MinMax<C extends Comparable<? super C>> 
		extends AdaptableAccumulator<C> 
	{
		private C _min;
		private C _max;
		
		public MinMax() {
		}
		
		public C getMin() {
			return _min;
		}
		
		public C getMax() {
			return _max;
		}
		
		/**
		 * @throws NullPointerException if the given {@code value} is {@code null}.
		 */
		@Override
		public void accumulate(final C value) {
			if (_min == null) {
				_min = value;
				_max = value;
			} else {
				if (value.compareTo(_min) < 0) {
					_min = value;
				} else if (value.compareTo(_max) > 0) {
					_max = value;
				}
			}
			
			++_samples;
		}
		
		@Override
		public String toString() {
			return String.format(
					"%s[samples=%d, min=%s, max=%s]", 
					getClass().getSimpleName(), getSamples(), getMin(), getMax()
				);
		}
	}
	
	/**
	 * Calls the {@link Accumulator#accumulate(Object)} method of all given
	 * {@code accumulators} with each value of the given {@code values}. The 
	 * accumulation is done in parallel.
	 * 
	 * @param <T> the value type.
	 * @param values the values to accumulate.
	 * @param accumulators the accumulators to apply.
	 * @throws NullPointerException if one of the given arguments is {@code null}.
	 */
	public static <T> void accumulate(
		final Iterable<? extends T> values, 
		final Array<Accumulator<? super T>> accumulators
	) {
		switch (accumulators.length()) {
		case 1:
			Accumulators.<T>accumulate(
					values, 
					accumulators.get(0)
				); 
			break;
		case 2:
			Accumulators.<T>accumulate(
					values, 
					accumulators.get(0), 
					accumulators.get(1)
				); 
			break;
		case 3:
			Accumulators.<T>accumulate(
					values, 
					accumulators.get(0), 
					accumulators.get(1),
					accumulators.get(2)
				); 
			break;
		case 4:
			Accumulators.<T>accumulate(
					values, 
					accumulators.get(0), 
					accumulators.get(1),
					accumulators.get(2),
					accumulators.get(3)
				);
			break;
		case 5:
			Accumulators.<T>accumulate(
					values, 
					accumulators.get(0), 
					accumulators.get(1),
					accumulators.get(2),
					accumulators.get(3),
					accumulators.get(4)
				); 
			break;
		default:
			ConcurrentContext.enter();
			try {
				for (final Accumulator<? super T> accumulator : accumulators) {
					ConcurrentContext.execute(new Acc<T>(values, accumulator));
				}
			} finally {
				ConcurrentContext.exit();
			}
		}
	}
	
	/**
	 * Calls the {@link Accumulator#accumulate(Object)} method of the given
	 * {@code accumulator} with each value of the given {@code values}. 
	 * 
	 * @param <T> the value type.
	 * @param values the values to accumulate.
	 * @param a the accumulator.
	 * @throws NullPointerException if one of the given arguments is {@code null}.
	 */
	public static <T> void accumulate(
		final Iterable<? extends T> values,
		final Accumulator<? super T> a
	) {
		for (final T value : values) {
			a.accumulate(value);
		}
	}
	
	/**
	 * Calls the {@link Accumulator#accumulate(Object)} method of all given
	 * {@code accumulators} with each value of the given {@code values}. The 
	 * accumulation is done in parallel.
	 * 
	 * @param <T> the value type.
	 * @param values the values to accumulate.
	 * @param a1 the first accumulator.
	 * @param a2 the second accumulator.
	 * @throws NullPointerException if one of the given arguments is {@code null}.
	 */
	public static <T> void accumulate(
		final Iterable<? extends T> values,
		final Accumulator<? super T> a1,
		final Accumulator<? super T> a2
	) {
		ConcurrentContext.enter();
		try {
			ConcurrentContext.execute(new Acc<T>(values, a1));
			ConcurrentContext.execute(new Acc<T>(values, a2));;			
		} finally {
			ConcurrentContext.exit();
		}
	}
	
	/**
	 * Calls the {@link Accumulator#accumulate(Object)} method of all given
	 * {@code accumulators} with each value of the given {@code values}. The 
	 * accumulation is done in parallel.
	 * 
	 * @param <T> the value type.
	 * @param values the values to accumulate.
	 * @param a1 the first accumulator.
	 * @param a2 the second accumulator.
	 * @param a3 the third accumulator
	 * @throws NullPointerException if one of the given arguments is {@code null}.
	 */
	public static <T> void accumulate(
		final Iterable<? extends T> values,
		final Accumulator<? super T> a1,
		final Accumulator<? super T> a2,
		final Accumulator<? super T> a3
	) {
		ConcurrentContext.enter();
		try {
			ConcurrentContext.execute(new Acc<T>(values, a1));
			ConcurrentContext.execute(new Acc<T>(values, a2));
			ConcurrentContext.execute(new Acc<T>(values, a3));			
		} finally {
			ConcurrentContext.exit();
		}
	}
	
	/**
	 * Calls the {@link Accumulator#accumulate(Object)} method of all given
	 * {@code accumulators} with each value of the given {@code values}. The 
	 * accumulation is done in parallel.
	 * 
	 * @param <T> the value type.
	 * @param values the values to accumulate.
	 * @param a1 the first accumulator.
	 * @param a2 the second accumulator.
	 * @param a3 the third accumulator.
	 * @param a4 the fourth accumulator.
	 * @throws NullPointerException if one of the given arguments is {@code null}.
	 */
	public static <T> void accumulate(
		final Iterable<? extends T> values,
		final Accumulator<? super T> a1,
		final Accumulator<? super T> a2,
		final Accumulator<? super T> a3,
		final Accumulator<? super T> a4
	) {
		ConcurrentContext.enter();
		try {
			ConcurrentContext.execute(new Acc<T>(values, a1));
			ConcurrentContext.execute(new Acc<T>(values, a2));
			ConcurrentContext.execute(new Acc<T>(values, a3));
			ConcurrentContext.execute(new Acc<T>(values, a4));	
		} finally {
			ConcurrentContext.exit();
		}
	}
	
	/**
	 * Calls the {@link Accumulator#accumulate(Object)} method of all given
	 * {@code accumulators} with each value of the given {@code values}. The 
	 * accumulation is done in parallel.
	 * 
	 * @param <T> the value type.
	 * @param values the values to accumulate.
	 * @param a1 the first accumulator.
	 * @param a2 the second accumulator.
	 * @param a3 the third accumulator.
	 * @param a4 the fourth accumulator.
	 * @param a5 the fifth accumulator.
	 * @throws NullPointerException if one of the given arguments is {@code null}.
	 */
	public static <T> void accumulate(
		final Iterable<? extends T> values,
		final Accumulator<? super T> a1,
		final Accumulator<? super T> a2,
		final Accumulator<? super T> a3,
		final Accumulator<? super T> a4,
		final Accumulator<? super T> a5
	) {
		ConcurrentContext.enter();
		try {	
			ConcurrentContext.execute(new Acc<T>(values, a1));
			ConcurrentContext.execute(new Acc<T>(values, a2));
			ConcurrentContext.execute(new Acc<T>(values, a3));
			ConcurrentContext.execute(new Acc<T>(values, a4));
			ConcurrentContext.execute(new Acc<T>(values, a5));
		} finally {
			ConcurrentContext.exit();
		}
	}	
	
	private static class Acc<T> implements Runnable {
		private final Iterable<? extends T> _values;
		private final Accumulator<? super T> _accumulator;
		
		public Acc(
			final Iterable<? extends T> values,
			final Accumulator<? super T> accumulator
		) {
			_values = values;
			_accumulator = accumulator;
		}
		
		@Override
		public void run() {
			for (final T value : _values) {
				_accumulator.accumulate(value);
			}
		}
	}
	
}


