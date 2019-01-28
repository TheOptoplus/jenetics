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
package io.jenetics.ext.util;

import static io.jenetics.ext.util.TreeMatcher.children;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.jenetics.ext.util.Tree.Path;
import io.jenetics.ext.util.TreeRewriter.Matcher;

/**
 * Checks if the given list of sub-trees are equals within the given tree.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class SubTreeMatcher<V> implements Matcher<V> {

	private final List<List<Path>> _subTrees;

	private SubTreeMatcher(final List<List<Path>> subTrees) {
		_subTrees = subTrees;
	}

	@Override
	public boolean matches(final Tree<V, ?> tree) {
		return _subTrees.stream().allMatch(sub -> equals(tree, sub));
	}

	private static boolean
	equals(final Tree<?, ?> tree, final List<Path> paths) {
		final List<Tree<?, ?>> nodes = children(tree, paths);

		boolean matches = nodes.size() == paths.size();
		final Iterator<Tree<?, ?>> it = nodes.iterator();
		if (it.hasNext()) {
			final Tree<?, ?> tn = it.next();
			while (matches && it.hasNext()) {
				matches = Objects.equals(tn, it.next());
			}
		}

		return matches;
	}

	static <V> SubTreeMatcher<V> of(final Tree<String, ?> pattern) {
		final Map<String, List<Tree<String, ?>>> leafs = pattern.stream()
			.filter(SubTreeMatcher::isVariable)
			.collect(Collectors.groupingBy(Tree::getValue));

		final List<List<Path>> paths = leafs.values().stream()
			.map(l -> l.stream()
				.map(Tree::childPath)
				.collect(Collectors.toList()))
			.collect(Collectors.toList());

		return new SubTreeMatcher<>(paths);
	}

	static boolean isVariable(final Tree<String, ?> node) {
		return node.isLeaf() &&
			node.getValue().length() == 1 &&
			Character.isLetter(node.getValue().charAt(0)) &&
			Character.isUpperCase(node.getValue().charAt(0));
	}

}