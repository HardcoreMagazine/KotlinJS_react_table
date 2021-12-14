package components

import data.Student
import kotlinext.js.jso
import kotlinx.css.*
import react.*
import react.dom.*
import react.table.*
import styled.*

/** --------------------------------- **
 * main component: useRowSelect (alt) *
 * ---------------------------------- **/

//interface StudentProps implemented in cBasicTable

val cTablePlusSelect = fc("TablePlusSelect") { p: StudentProps ->
    /** define cells data **/
    val students = p.students
    /** define columns && link them to values (class fields) **/
    val studColumns = useMemo {
        //hook useMemo returns memorized DATA that ~never changes, i.e. cached DATA
        //!! is required by useTables hook !!
        columns<Student> {
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
        }
        //},
        //useRowSelect //same feature implemented in useTable hook, see code below
    )
    /** selected elem ~pointer **/
    val (selected, setSelected) = useState<Array<Student>>() //allows auto-updates on row click
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
                            val origin = h.placeholderOf
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
            styledTbody {
                for (row in table.rows) {
                    table.prepareRow(row)
                    tr {
                        attrs.onClick = {
                            val stud = row.original
                            /** 'row.original' - pull original data from row [useRowSelect analog] **/
                            if (selected == null) setSelected(arrayOf(stud))
                            else if (selected.contains(stud)) setSelected((selected.toList()-stud).toTypedArray())
                            else setSelected(selected+stud)
                        }
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
    div {
        //print DATA from selected rows
        if (selected == null || selected.contentEquals(arrayOf())) +""
        else {
            +"Selected:"
            ul {
                for (student in selected)
                    li { +student.toString() }
            }
        }
    }
}

fun RBuilder.makeTablePlusSelect(ss: Array<Student>) = child(cTablePlusSelect) {
    attrs.students = ss
}
