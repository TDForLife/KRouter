package com.github.richardwrq.krouter.gradle.plugin

import com.android.build.gradle.api.AndroidBasePlugin
import com.github.richardwrq.krouter.annotation.UtilsKt
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author WuRuiQiang <a href="mailto:263454190@qq.com">Contact me.</a>
 * @version v1.0
 * @since 18/2/28 下午3:37
 */
class KRouterPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def kaptExtension = project.extensions.getByName('kapt')
        def androidExtension = project.extensions.getByName("android")
        if (kaptExtension != null) {
            kaptExtension.arguments {
                arg(UtilsKt.MODULE_NAME, project.getName())
            }
        }

        project.configurations.all { configuration ->
            def name = configuration.name
            if (name == "implementation" || name == "compile") {
                System.out.printf("Add krouter-api dependency")
                configuration.dependencies.add(project.dependencies.create(Const.KROUTER_API))
            } else if (name == "kapt") {
                System.out.printf("Add krouter-compiler dependency")
                configuration.dependencies.add(project.dependencies.create(Const.KROUTER_COMPILER))
            }
        }

        project.afterEvaluate {
            if (!project.plugins.hasPlugin(AndroidBasePlugin.class)) {
                return
            }
            def assetPath = project.path + "/assets"
            for (dir in androidExtension.sourceSets.main.assets.getSrcDirs()) {
                if (dir.path != null && dir.path.length() > 0) {
                    assetPath = dir.path
                }
            }
            def file = new File(assetPath + "/${UtilsKt.PROJECT_NAME}${UtilsKt.SEPARATOR}" + project.name)
            file.parentFile.mkdirs()
            file.createNewFile()
        }

    }
}
