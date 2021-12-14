package data

data class Student(var id: Int, val firstname: String, val surname: String, var grade: Byte = 0) {
    override fun toString(): String {
        return "ID: $id; FULL NAME: $firstname $surname; GRADE: $grade;"
    }
}
