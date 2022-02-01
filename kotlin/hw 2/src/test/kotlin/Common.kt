import kastree.ast.Node
import kastree.ast.psi.Parser
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiErrorElement
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType

fun Parser.myParseFile(code: String, throwOnError: Boolean = true): Node.File {
    val configuration = CompilerConfiguration()
    configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

    val project = KotlinCoreEnvironment.createForProduction(
        Disposer.newDisposable(),
        configuration,
        EnvironmentConfigFiles.JVM_CONFIG_FILES
    ).project

    val parseResult =
        PsiManager.getInstance(project).findFile(LightVirtualFile("temp.kt", KotlinFileType.INSTANCE, code)) as KtFile
    return converter.convertFile(parseResult.also { file ->
        if (throwOnError) file.collectDescendantsOfType<PsiErrorElement>().let {
            if (it.isNotEmpty()) throw Parser.ParseError(file, it)
        }
    })
}