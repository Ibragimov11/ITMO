@file:Suppress("FoldInitializerAndIfToElvis")

/**
 * TODO Упростите эту функцию так, чтобы в ней осталось не более одного утверждения 'if'.
 */
fun sendMessageToClient(
    client: Client?,
    message: String?,
    mailer: Mailer
) {
    message ?: return

    val personalInfo = client?.personalInfo ?: return

    val email = personalInfo.email ?: return

    mailer.sendMessage(email, message)
}

class Client(val personalInfo: PersonalInfo?)

class PersonalInfo(val email: String?)

interface Mailer {
    fun sendMessage(email: String, message: String)
}
