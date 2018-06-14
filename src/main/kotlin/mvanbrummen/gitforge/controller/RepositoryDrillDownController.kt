package mvanbrummen.gitforge.controller

import mvanbrummen.gitforge.service.RepositoryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.servlet.http.HttpServletRequest

@Controller
class RepositoryDrillDownController(private val repositoryService: RepositoryService) {

    @GetMapping("/repo/{accountName}/{repoName}/blob/**")
    fun drillDownPage(@PathVariable accountName: String, @PathVariable repoName: String, model: Model,
                      httpServletRequest: HttpServletRequest): String {

        val filePath = httpServletRequest.requestURI.replace(Regex("/repo.*blob/"), "")
        val pathSegments = filePath.split("/")

        val pathSegmentMap = pathSegments.map {
            it to pathSegments.subList(0, pathSegments.indexOf(it) + 1).joinToString("/")
        }.toMap()

        val items = repositoryService.getRepositoryItemsByPath(accountName, repoName, filePath)

        model.addAttribute("filePath", filePath)
        model.addAttribute("repoName", repoName)
        model.addAttribute("pathSegmentMap", pathSegmentMap)

        return if (items.isEmpty()) {
            val fileContents = repositoryService.getFileContentsByPath(accountName, repoName, filePath)
            model.addAttribute("fileContents", fileContents)
            "fileView"
        } else {
            val parentPath = if (pathSegments.size == 1) "/repo/$accountName/$repoName"
            else {
                val segments = pathSegments.dropLast(1).joinToString("/")

                "/repo/$accountName/$repoName/blob/$segments"
            }
            model.addAttribute("parentPath", parentPath)
            model.addAttribute("items", items)
            "repositoryDrilldown"
        }
    }
}