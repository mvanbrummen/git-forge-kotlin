package mvanbrummen.gitforge.util

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

private val parser = Parser.builder().build()
private val renderer = HtmlRenderer.builder().build()

fun String.toHtml(): String {
    return renderer.render(parser.parse(this))
}


