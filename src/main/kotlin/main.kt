import components.makeBasicTable
import components.makeTablePlusSelect
import components.makeTablePlusSort
import data.studentArray
import kotlinx.browser.document
import react.dom.*

// kotlin wrapper for react-table:
// https://github.com/JetBrains/kotlin-wrappers/blob/master/kotlin-react-table/README.md
// JavaScript documentation && examples:
// https://react-table.tanstack.com/docs/overview

fun main() {
    render(document.getElementById("root")) {
        h3 { +"Basic table" }
        makeBasicTable(studentArray)
        h3 { +"Table + row select" }
        makeTablePlusSelect(studentArray)
        h3 { +"Table + sort" }
        makeTablePlusSort(studentArray)
    }
}
