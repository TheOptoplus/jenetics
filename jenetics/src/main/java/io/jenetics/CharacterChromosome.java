/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics;

import static io.jenetics.CharacterGene.DEFAULT_CHARACTERS;
import static io.jenetics.internal.util.Hashes.hash;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.jenetics.internal.util.IntRef;
import io.jenetics.internal.util.reflect;
import io.jenetics.util.CharSeq;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import io.jenetics.util.MSeq;

/**
 * CharacterChromosome which represents character sequences.
 *
 * @see CharacterGene
 *
 * @implNote
 * This class is immutable and thread-safe.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 1.0
 * @version 4.3
 */
public class CharacterChromosome
	extends
		VariableChromosome<CharacterGene>
	implements
		CharSequence,
		Serializable
{
	private static final long serialVersionUID = 3L;

	private transient CharSeq _validCharacters;

	/**
	 * Create a new chromosome from the given {@code genes} array. The genes
	 * array is copied, so changes to the given genes array doesn't effect the
	 * genes of this chromosome.
	 *
	 * @since 4.0
	 *
	 * @param genes the genes that form the chromosome.
	 * @param lengthRange the allowed length range of the chromosome.
	 * @throws NullPointerException if the given gene array is {@code null}.
	 * @throws IllegalArgumentException if the length of the gene array is
	 *         smaller than one.
	 */
	protected CharacterChromosome(
		final ISeq<CharacterGene> genes,
		final IntRange lengthRange
	) {
		super(genes, lengthRange);
		_validCharacters = genes.get(0).getValidCharacters();
	}

	/**
	 * Create a new chromosome with the {@code validCharacters} char set as
	 * valid characters.
	 *
	 * @since 4.0
	 *
	 * @param validCharacters the valid characters for this chromosome.
	 * @param lengthRange the allowed length range of the chromosome.
	 * @throws NullPointerException if the {@code validCharacters} is
	 *         {@code null}.
	 * @throws IllegalArgumentException if the length of the gene sequence is
	 *         empty, doesn't match with the allowed length range, the minimum
	 *         or maximum of the range is smaller or equal zero or the given
	 *         range size is zero.
	 *
	 * @deprecated Use {@link #of(CharSeq, IntRange)} instead.
	 */
	@Deprecated
	public CharacterChromosome(
		final CharSeq validCharacters,
		final IntRange lengthRange
	) {
		this(CharacterGene.seq(validCharacters, lengthRange), lengthRange);
		_valid = true;
	}

	/**
	 * Create a new chromosome with the {@code validCharacters} char set as
	 * valid characters.
	 *
	 * @param validCharacters the valid characters for this chromosome.
	 * @param length the length of the chromosome.
	 * @throws NullPointerException if the {@code validCharacters} is
	 *         {@code null}.
	 * @throws IllegalArgumentException if the length of the gene sequence is
	 *         empty, doesn't match with the allowed length range, the minimum
	 *         or maximum of the range is smaller or equal zero or the given
	 *         range size is zero.
	 *
	 * @deprecated Use {@link #of(CharSeq, int)} instead.
	 */
	@Deprecated
	public CharacterChromosome(
		final CharSeq validCharacters,
		final int length
	) {
		this(validCharacters, IntRange.of(length));
	}

	@Override
	public char charAt(final int index) {
		return getGene(index).charValue();
	}

	@Override
	public CharacterChromosome subSequence(final int start, final int end) {
		return new CharacterChromosome(_genes.subSeq(start, end), lengthRange());
	}

	/**
	 * @throws NullPointerException if the given gene array is {@code null}.
	 */
	@Override
	public CharacterChromosome newInstance(final ISeq<CharacterGene> genes) {
		return new CharacterChromosome(genes, lengthRange());
	}

	/**
	 * Create a new, <em>random</em> chromosome.
	 */
	@Override
	public CharacterChromosome newInstance() {
		return of(_validCharacters, lengthRange());
	}

	@Override
	public int hashCode() {
		return hash(super.hashCode(), hash(_validCharacters));
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this ||
			obj != null &&
			getClass() == obj.getClass() &&
			Objects.equals(_validCharacters, ((CharacterChromosome)obj)._validCharacters) &&
			super.equals(obj);
	}

	@Override
	public String toString() {
		return new String(toArray());
	}

	/**
	 * Returns an char array containing all of the elements in this chromosome
	 * in proper sequence.  If the chromosome fits in the specified array, it is
	 * returned therein. Otherwise, a new array is allocated with the length of
	 * this chromosome.
	 *
	 * @since 3.0
	 *
	 * @param array the array into which the elements of this chromosomes are to
	 *        be stored, if it is big enough; otherwise, a new array is
	 *        allocated for this purpose.
	 * @return an array containing the elements of this chromosome
	 * @throws NullPointerException if the given {@code array} is {@code null}
	 */
	public char[] toArray(final char[] array) {
		final char[] a = array.length >= length() ?
			array : new char[length()];

		for (int i = length(); --i >= 0;) {
			a[i] = charAt(i);
		}

		return a;
	}

	/**
	 * Returns an char array containing all of the elements in this chromosome
	 * in proper sequence.
	 *
	 * @since 3.0
	 *
	 * @return an array containing the elements of this chromosome
	 */
	public char[] toArray() {
		return toArray(new char[length()]);
	}


	/* *************************************************************************
	 * Static factory methods.
	 * ************************************************************************/

	/**
	 * Create a new chromosome with the {@code validCharacters} char set as
	 * valid characters.
	 *
	 * @since 4.3
	 *
	 * @param validCharacters the valid characters for this chromosome.
	 * @param lengthRange the allowed length range of the chromosome.
	 * @return a new {@code CharacterChromosome} with the given parameter
	 * @throws NullPointerException if the {@code validCharacters} is
	 *         {@code null}.
	 * @throws IllegalArgumentException if the length of the gene sequence is
	 *         empty, doesn't match with the allowed length range, the minimum
	 *         or maximum of the range is smaller or equal zero or the given
	 *         range size is zero.
	 */
	public static CharacterChromosome of(
		final CharSeq validCharacters,
		final IntRange lengthRange
	) {
		return new CharacterChromosome(
			CharacterGene.seq(validCharacters, lengthRange),
			lengthRange
		);
	}

	/**
	 * Create a new chromosome with the {@link CharacterGene#DEFAULT_CHARACTERS}
	 * char set as valid characters.
	 *
	 * @param lengthRange the allowed length range of the chromosome.
	 * @return a new {@code CharacterChromosome} with the given parameter
	 * @throws IllegalArgumentException if the {@code length} is smaller than
	 *         one.
	 */
	public static CharacterChromosome of(final IntRange lengthRange) {
		return of(DEFAULT_CHARACTERS, lengthRange);
	}

	/**
	 * Create a new chromosome with the {@code validCharacters} char set as
	 * valid characters.
	 *
	 * @since 4.3
	 *
	 * @param validCharacters the valid characters for this chromosome.
	 * @param length the {@code length} of the new chromosome.
	 * @return a new {@code CharacterChromosome} with the given parameter
	 * @throws NullPointerException if the {@code validCharacters} is
	 *         {@code null}.
	 * @throws IllegalArgumentException if the length of the gene sequence is
	 *         empty, doesn't match with the allowed length range, the minimum
	 *         or maximum of the range is smaller or equal zero or the given
	 *         range size is zero.
	 */
	public static CharacterChromosome of(
		final CharSeq validCharacters,
		final int length
	) {
		return of(validCharacters, IntRange.of(length));
	}

	/**
	 * Create a new chromosome with the {@link CharacterGene#DEFAULT_CHARACTERS}
	 * char set as valid characters.
	 *
	 * @param length the {@code length} of the new chromosome.
	 * @return a new {@code CharacterChromosome} with the given parameter
	 * @throws IllegalArgumentException if the {@code length} is smaller than
	 *         one.
	 */
	public static CharacterChromosome of(final int length) {
		return of(DEFAULT_CHARACTERS, length);
	}

	/**
	 * Create a new chromosome from the given genes (given as string).
	 *
	 * @param alleles the character genes.
	 * @param validChars the valid characters.
	 * @return a new {@code CharacterChromosome} with the given parameter
	 * @throws IllegalArgumentException if the genes string is empty.
	 */
	public static CharacterChromosome of(
		final String alleles,
		final CharSeq validChars
	) {
		final IntRef index = new IntRef();
		final Supplier<CharacterGene> geneFactory = () -> CharacterGene.of(
			alleles.charAt(index.value++), validChars
		);

		final ISeq<CharacterGene> genes =
			MSeq.<CharacterGene>ofLength(alleles.length())
				.fill(geneFactory)
				.toISeq();

		return new CharacterChromosome(genes, IntRange.of(alleles.length()));
	}

	/**
	 * Create a new chromosome from the given genes (given as string).
	 *
	 * @param alleles the character genes.
	 * @return a new {@code CharacterChromosome} with the given parameter
	 * @throws IllegalArgumentException if the genes string is empty.
	 */
	public static CharacterChromosome of(final String alleles) {
		return of(alleles, DEFAULT_CHARACTERS);
	}


	/* *************************************************************************
	 *  Java object serialization
	 * ************************************************************************/

	private void writeObject(final ObjectOutputStream out)
		throws IOException
	{
		out.defaultWriteObject();

		out.writeInt(length());
		out.writeObject(_validCharacters);

		for (CharacterGene gene : _genes) {
			out.writeChar(gene.getAllele());
		}
	}

	private void readObject(final ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		final int length = in.readInt();
		_validCharacters = (CharSeq)in.readObject();

		final MSeq<CharacterGene> genes = MSeq.ofLength(length);
		for (int i = 0; i < length; ++i) {
			final CharacterGene gene = CharacterGene.of(
				in.readChar(),
				_validCharacters
			);
			genes.set(i, gene);
		}
		reflect.setField(this, "_genes", genes.toISeq());
	}

}
