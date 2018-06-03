package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.ArchiveService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.zeroturnaround.zip.commons.IOUtils
import java.io.FileInputStream
import javax.servlet.http.HttpServletResponse

@Controller
class ArchiveController(private val archiveService: ArchiveService) {

    @RequestMapping(value = "/repo/{accountName}/{repoName}/zip", produces = arrayOf("application/zip"))
    fun getZip(@PathVariable accountName: String, @PathVariable repoName: String, response: HttpServletResponse) {
        val fileName = "$accountName-$repoName.zip"

        val inputStream = FileInputStream(archiveService.getZip(accountName, repoName, fileName))

        response.setStatus(HttpServletResponse.SC_OK)
        response.addHeader("Content-Disposition", "attachment; filename=\"$fileName\"")

        IOUtils.copy(inputStream, response.outputStream)
        response.flushBuffer()
    }
}