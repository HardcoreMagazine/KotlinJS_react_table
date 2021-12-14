package components

import data.Student
import kotlinext.js.jso
import kotlinx.css.*
import react.*
import react.dom.*
import react.table.RenderType
import react.table.columns
import react.table.useTable
import styled.*

/** ----------------------- **
 * main component: useTable *
 * ------------------------ **/

external interface StudentProps: Props {
    var students: Array<Student> //must be array type -- mutable*** && lists wont work for some reason
}

val cBasicTable = fc("BasicTable") { p: StudentProps ->
    /** define cells data **/
    val students = p.students
    /** define columns && link them to values (class fields) **/
    val studColumns = useMemo {
        //hook useMemo returns memorized DATA that ~never changes, i.e. cached DATA
        //!! is required by useTables hook !!
        columns<Student> { //define "global" column based on data class
            column<Int> { //define subColumn of cell type Int
                header = "ID"
                accessorFunction = { it.id } //column*row (cell) data of type Int
            }
            column<String> {
                header = "Student"
                accessorFunction = { "${it.firstname} ${it.surname}" }
            }
            column<String> {
                header = "Grade"
                accessorFunction = { it.grade.toString() }
            }
        }
    }
    /** main table hook **/
    val table = useTable<Student>(
        options = jso { //jso - creates JS Object
            this.data = students
            this.columns = studColumns
        }
    )
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
            /** generating headers **/
            styledThead {
                css {
                    fontSize = 14.px
                }
                for (hg in table.headerGroups) {
                    tr {
                        for (h in hg.headers) {
                            //h - instance of column, contains HEADER_TEXT && cell formation instruction
                            val origin = h.placeholderOf //may return null
                            val header = origin ?: h
                            styledTh {
                                css {
                                    border = "1px solid black"
                                }
                                +header.render(RenderType.Header)
                            }
                        }
                    }
                }
            }
            /** generating rows **/
            styledTbody {
                for (row in table.rows) {
                    table.prepareRow(row) //preps row for rendering (?) -- mandatory
                    tr {
                        for (cell in row.cells) { //same idea as headers but with cells (see comment above)
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

fun RBuilder.makeBasicTable(ss: Array<Student>) = child(cBasicTable) {
    attrs.students = ss
}
