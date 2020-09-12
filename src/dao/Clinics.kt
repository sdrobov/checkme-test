package com.checkme.test.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Clinics: IntIdTable() {
    val name = varchar("name", 255)
}

class Clinic(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Clinic>(Clinics)

    var name by Clinics.name
}

data class ClinicData(val id: Int?, val name: String, val checkUps: Array<ClinicCheckUpData> = emptyArray()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClinicData

        if (id != other.id) return false
        if (name != other.name) return false
        if (!checkUps.contentEquals(other.checkUps)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + checkUps.contentHashCode()
        return result
    }
}
