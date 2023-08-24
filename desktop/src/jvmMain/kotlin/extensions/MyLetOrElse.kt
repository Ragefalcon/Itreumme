package extensions

inline fun <T : Any> T?.myLetOrElse(
    block: (T) -> Unit,
    elseBlock: () -> Unit
) {
    if (this != null) {
        block(this)
    } else {
        elseBlock()
    }
}