package com.checkme.test.services

import com.checkme.test.dao.*
import com.checkme.test.dao.CheckUps
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object CheckUps {
    fun get(id: Int): CheckUpData? {
        val checkUp = transaction {
            CheckUp.findById(id)
        } ?: return null

        return CheckUpData(checkUp.id.value, checkUp.name, clinics(checkUp))
    }

    fun all(): Array<CheckUpData> {
        return transaction {
            CheckUp.all()
                .map { CheckUpData(it.id.value, it.name, clinics(it)) }
                .toTypedArray()
        }
    }

    fun save(data: CheckUpData): CheckUpData {
        val checkUp = transaction {
            CheckUp.new {
                name = data.name
            }
        }

        return CheckUpData(checkUp.id.value, checkUp.name, emptyArray())
    }

    fun update(id: Int, data: CheckUpData): CheckUpData? {
        val checkUp = transaction {
            val checkUp = CheckUp.findById(id) ?: return@transaction null

            checkUp.name = data.name

            return@transaction checkUp
        } ?: return null

        return CheckUpData(checkUp.id.value, checkUp.name, clinics(checkUp))
    }

    fun delete(id: Int) {
        transaction {
            CheckUps.deleteWhere { CheckUps.id eq id }
        }
    }

    private fun clinics(checkUp: CheckUp): Array<CheckUpClinicData> {
        return transaction {
            Join(
                ClinicsCheckUps, com.checkme.test.dao.Clinics,
                onColumn = ClinicsCheckUps.clinic,
                otherColumn = com.checkme.test.dao.Clinics.id,
                joinType = JoinType.INNER
            )
                .select { ClinicsCheckUps.checkUp eq checkUp.id.value }
                .map {
                    CheckUpClinicData(
                        it[ClinicsCheckUps.checkUp],
                        it[com.checkme.test.dao.Clinics.name],
                        it[ClinicsCheckUps.price]
                    )
                }
                .toTypedArray()
        }
    }
}
