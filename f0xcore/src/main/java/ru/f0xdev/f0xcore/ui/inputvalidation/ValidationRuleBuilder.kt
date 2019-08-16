package ru.f0xdev.f0xcore.ui.inputvalidation

import ru.f0xdev.f0xcore.ui.inputvalidation.ValidationRuleBuilder.ARG_DELIMITER
import ru.f0xdev.f0xcore.ui.inputvalidation.ValidationRuleBuilder.RULE_DELIMITER
import ru.f0xdev.f0xcore.ui.inputvalidation.ValidationRuleBuilder.ruleMap
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl.*

object ValidationRuleBuilder {
    val ruleMap = mutableMapOf(
        "required" to RequiredRule::class.java,
        "regex" to RegexRule::class.java,
        "regex_allow_empty" to RegexAllowEmptyRule::class.java,
        "min" to MinRule::class.java,
        "max" to MaxRule::class.java,
        "integer" to IntegerRule::class.java,
        "between" to BetweenRule::class.java,
        "email" to EmailRule::class.java,
        "min_length" to MinLengthRule::class.java,
        "checked" to CheckedRule::class.java
    )
    const val RULE_DELIMITER = "|"
    const val ARG_DELIMITER = ":"

}

fun buildValidationRules(validationString: String): List<InputValidationRule> {
    val result = mutableListOf<InputValidationRule>()
    validationString.split(RULE_DELIMITER)
        .forEach { ruleString ->
            var key: String? = null
            var arg: String? = null
            val split = ruleString.split(ARG_DELIMITER)
            split.forEachIndexed { index, s ->
                when (index) {
                    0 -> key = s
                    1 -> arg = s
                }
            }
            createRule(key ?: "", arg ?: "")?.let { result.add(it) }
        }
    return result
}


fun addRule(name: String, ruleClass: Class<out InputValidationRule>) {
    ruleMap[name] = ruleClass
}

private fun createRule(ruleKey: String, ruleArg: String): InputValidationRule? {
    ruleMap[ruleKey]?.let { clazz ->
        val constructor = Class.forName(clazz.name).getConstructor(String::class.java)
        val rule = constructor.newInstance(ruleArg)
        if (rule is InputValidationRule) {
            return rule
        }
    }
    return null
}