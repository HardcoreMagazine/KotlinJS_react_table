package components

import data.Student
import kotlinext.js.jso
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.*
import react.fc
import react.table.*
import react.useMemo
import styled.*

/** ------------------------ **
 * main component: useSortBy *
 * ------------------------- **/

//interface StudentProps implemented in cBasicTable

val cTablePlusState = fc("TablePlusSort") { p: StudentProps ->
    /** define cells data **/
    val students = p.students
    /** define columns && link them to values (class fields) **/
    val studColumns = useMemo {
        //hook useMemo returns memorized DATA that ~never changes, i.e. cached DATA
        //!! is required by useTables hook !!
        columns<Student> {
            column<Int> {
                header = "ID"
                accessorFunction = { it.id }
            }
            column<String> {
                header = "Full name"
                accessorFunction = { "${it.firstname} ${it.surname}" }
            }
            column<Byte> {
                header = "Grade"
                accessorFunction = { it.grade }
            }
        }
    }
    /** table hook **/
    val table = useTable<Student>(
        options = jso { //jso - JSObject
            this.data = students
            this.columns = studColumns
        },
        useSortBy //is a PLUGIN; optional
        //plugins = arrayOf(useSortBy) //same thing as above
    )
    /** sort hook/plugin >>>>> attached TO TABLE itself, does nothing on its own **/
    //note: each sort resets (like: not sorted -> sortByDesc -> sortByAsc -> not sorted)
    //^^note based on JavaScript REACT-TABLE implementation
    /** table formation **/
    div {
        styledTable {
            css {
                width = 300.px
                borderSpacing = 0.px
                borderCollapse = BorderCollapse.collapse
                whiteSpace = WhiteSpace.nowrap
                border = "1px solid black"
                margin = "auto"
            }
            styledThead {
                css {
                    fontSize = 14.px
                }
                for (headersArray in table.headerGroups) {
                    tr {
                        for (h in headersArray.headers) {
                            val columnInstance = h.placeholderOf //may return null
                            val header = columnInstance ?: h
                            styledTh {
                                css {
                                    border = "1px solid black"
                                }
                                +header.render(RenderType.Header)
                                //var sort: String
                                attrs.onClickFunction = {
                                    if(header.isSorted) {
                                        header.toggleSortBy(true, multi = false)
                                        //sort = "v sortByAsc -> sortByDesc"
                                    }
                                    else if (header.isSortedDesc) {
                                        header.toggleSortBy(false, multi = false)
                                        //sort = "^ sortByDesc -> sortByAsc"
                                    }
                                    else {
                                        header.toggleSortBy(false, multi = false)
                                        //sort = "n/s -> sortByAsc"
                                    }
                                    //note: multi == multi-sort (does exactly as comment says)
                                    //println(sort)
                                }
                            }
                        }
                    }
                }
            }
            styledTbody {
                for (row in table.rows) {
                    table.prepareRow(row)
                    tr {
                        for (cell in row.cells) {
                            styledTd {
                                css {
                                    border = "1px solid black"
                                }
                                +cell.render(RenderType.Cell)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.makeTablePlusSort(ss: Array<Student>) = child(cTablePlusState) {
    attrs.students = ss
}
