package com.checkme.test.dao

import org.jetbrains.exposed.sql.Table

object ClinicsCheckUps: Table() {
    val clinic = integer("clinic")
    val checkUp = integer("checkup")
    val price = double("price")
    override val primaryKey = PrimaryKey(clinic, checkUp, name = "ClinicsCheckUps_pk")
}

data class ClinicCheckUpData(val id: Int, val name: String, val price: Double)
data class CheckUpClinicData(val id: Int, val name: String, val price: Double)
data class AddClinicCheckUpData(val id: Int, val price: Double)
