package com.checkme.test.services

import com.checkme.test.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Clinics {
    fun get(id: Int): ClinicData? {
        val clinic = transaction {
            Clinic.findById(id)
        } ?: return null

        return ClinicData(clinic.id.value, clinic.name, checkUps(clinic))
    }

    fun all(): Array<ClinicData> {
        return transaction {
            Clinic.all()
                .map { ClinicData(it.id.value, it.name, checkUps(it)) }
                .toTypedArray()
        }
    }

    fun save(data: ClinicData): ClinicData {
        val clinic = transaction {
            Clinic.new {
                name = data.name
            }
        }

        return ClinicData(clinic.id.value, clinic.name, emptyArray())
    }

    fun update(id: Int, data: ClinicData): ClinicData? {
        val clinic = transaction {
            val clinic = Clinic.findById(id) ?: return@transaction null

            clinic.name = data.name

            return@transaction clinic
        } ?: return null

        return ClinicData(clinic.id.value, clinic.name, checkUps(clinic))
    }

    fun delete(id: Int) {
        transaction {
            com.checkme.test.dao.Clinics.deleteWhere { com.checkme.test.dao.Clinics.id eq id }
        }
    }

    fun addCheckUp(id: Int, data: AddClinicCheckUpData): ClinicData? {
        try {
            transaction {
                ClinicsCheckUps.insert {
                    it[clinic] = id
                    it[checkUp] = data.id
                    it[price] = data.price
                }
            }
        } finally {
            return get(id)
        }
    }

    fun deleteCheckUp(clinicId: Int, checkUpId: Int): ClinicData? {
        transaction {
            ClinicsCheckUps.deleteWhere { ClinicsCheckUps.clinic eq clinicId and (ClinicsCheckUps.checkUp eq checkUpId) }
        }

        return get(clinicId)
    }

    private fun checkUps(clinic: Clinic): Array<ClinicCheckUpData> {
        return transaction {
            Join(
                ClinicsCheckUps, com.checkme.test.dao.CheckUps,
                onColumn = ClinicsCheckUps.checkUp,
                otherColumn = com.checkme.test.dao.CheckUps.id,
                joinType = JoinType.INNER
            )
                .select { ClinicsCheckUps.clinic eq clinic.id.value }
                .map {
                    ClinicCheckUpData(
                        it[ClinicsCheckUps.checkUp],
                        it[com.checkme.test.dao.CheckUps.name],
                        it[ClinicsCheckUps.price]
                    )
                }
                .toTypedArray()
        }
    }
}
