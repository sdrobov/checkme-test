package com.checkme.test.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CheckUps: IntIdTable() {
    val name = varchar("name", 255)
}

class CheckUp(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<CheckUp>(CheckUps)

    var name by CheckUps.name
}

data class CheckUpData(val id: Int?, val name: String, val clinics: Array<CheckUpClinicData> = emptyArray()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CheckUpData

        if (id != other.id) return false
        if (name != other.name) return false
        if (!clinics.contentEquals(other.clinics)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + clinics.contentHashCode()
        return result
    }
}
