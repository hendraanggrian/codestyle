package com.hanggrian.rulebook.ktlint

import com.hanggrian.rulebook.ktlint.internals.Messages
import com.pinterest.ktlint.rule.engine.core.api.ElementType.BLOCK
import com.pinterest.ktlint.rule.engine.core.api.ElementType.FUNCTION_LITERAL
import com.pinterest.ktlint.rule.engine.core.api.ElementType.LBRACE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.RBRACE
import com.pinterest.ktlint.rule.engine.core.api.children
import com.pinterest.ktlint.rule.engine.core.api.isLeaf
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpace
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.tree.TokenSet

/**
 * [See wiki](https://github.com/hanggrian/rulebook/wiki/Rules/#empty-code-block-conciseness)
 */
public class EmptyCodeBlockConcisenessRule : Rule("empty-code-block-conciseness") {
    override val tokens: TokenSet = TokenSet.create(BLOCK, FUNCTION_LITERAL)

    override fun visitToken(node: ASTNode, emit: Emit) {
        // checks for violation
        val children =
            node
                .takeIf {
                    !it.isLeaf() &&
                        it.firstChildNode.elementType == LBRACE &&
                        it.lastChildNode.elementType == RBRACE
                }?.children()
                ?.toMutableList()
                ?.apply {
                    removeFirst()
                    removeLast()
                    removeIf {
                        when (it.elementType) {
                            BLOCK -> it.children().count() == 0
                            else -> false
                        }
                    }
                }?.takeIf { nodes -> nodes.isNotEmpty() && nodes.all { it.isWhiteSpace() } }
                ?: return
        emit(children.first().startOffset, Messages[MSG], false)
    }

    internal companion object {
        const val MSG = "empty.code.block.conciseness"
    }
}
