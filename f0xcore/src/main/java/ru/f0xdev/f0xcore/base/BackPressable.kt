package ru.f0xdev.f0xcore.base

interface BackPressable {
    var listener: OnBackPressListener?
}

interface OnBackPressListener {
    /**
     * Листенер на фрагменты в которых обрабатывается нажатие back button
     * @return true если можно обработать выше в активити либо рутовых фрагментменеджерах
     * @return false если фрагмент сам обработает нажатие
     *
     * */
    fun onBackButtonPressed(): Boolean
}
