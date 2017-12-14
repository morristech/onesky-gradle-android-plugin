package com.github.sugoiwada.onesky

import com.github.sugoiwada.onesky.tasks.DownloadListingTask
import com.github.sugoiwada.onesky.tasks.DownloadStringsTask
import com.github.sugoiwada.onesky.tasks.UploadStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class OneskyPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            extensions.create("onesky", OneskyExtension::class.java)
            tasks.create("downloadStrings", DownloadStringsTask::class.java)
            tasks.create("uploadStrings", UploadStringsTask::class.java)
            tasks.create("downloadListing", DownloadListingTask::class.java)
        }
    }
}