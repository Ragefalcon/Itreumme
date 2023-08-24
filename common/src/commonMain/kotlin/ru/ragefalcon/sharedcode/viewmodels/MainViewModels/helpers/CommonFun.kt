package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.models.data.*

fun setMapStatikToItemYearGraf(statik: List<ItemRectDiagWithDate>, updF: (List<ItemYearGraf>) -> Unit) {
    if (statik.isNotEmpty()) {
        var dateStart: Int = DateTimeTz.fromUnixLocal(statik.firstOrNull()?.date ?: 0L).year.year //.unOffset()
        var dateEnd = DateTimeTz.fromUnixLocal(statik.lastOrNull()?.date ?: 0L).year.year //.unOffset()

        if (dateEnd<dateStart) {
            val tmp = dateStart
            dateStart = dateEnd
            dateEnd = tmp
        }

        val listRez: MutableList<ItemYearGraf> = mutableListOf()
        var aa = 10.0
        for (year in dateStart..dateEnd) {
            aa = statik.filter {
                DateTimeTz.fromUnixLocal(it.date).year.year == year //.unOffset()
            }.firstOrNull()?.sumyear ?: 0.0
            listRez.add(
                ItemYearGraf(
                    year,
                    statik.filter {
                        DateTimeTz.fromUnixLocal(it.date).year.year == year //.unOffset()
                    }.toMutableList().apply {
                        for (i in 1..12) {
                            if (this.find { DateTimeTz.fromUnixLocal(it.date).month1 == i } == null) {
                                this.add(
                                    ItemRectDiagWithDate(
                                        year.toString(),
                                        if (i<10) "0$i" else i.toString(),
                                        DateTimeTz.utc(DateTime(year,i,1),DateTimeTz.nowLocal().offset).localUnix(),
                                        0.0,
                                        aa,
                                        0.0
                                    )
                                )
                            }
                        }
                    }.sortedByDescending { it.date }.map {
                        ItemRectDiag(
                            it.year,
                            it.month,
                            it.summa,
                            it.sumyear,
                            it.procent
                        )
                    }
                )
            )
        }
        updF(listRez)
    } else {
        val year = DateTimeTz.nowLocal().year.year.toString()
        updF(
            mutableListOf(
                ItemYearGraf(
                    year.toInt(), listOf(
                        ItemRectDiag(year, "01", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "02", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "03", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "04", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "05", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "06", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "07", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "08", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "09", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "10", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "11", 0.0, 0.0, 0.0),
                        ItemRectDiag(year, "12", 0.0, 0.0, 0.0)
                    )
                )
            )
        )
    }
}

fun setMapStatikToItemYearGrafTwoRect(statik: List<ItemTwoRectDiagWithDate>, updF: (List<ItemYearGrafTwoRect>) -> Unit) {
    if (statik.isNotEmpty()) {
        var dateStart: Int = DateTimeTz.fromUnixLocal(statik.firstOrNull()?.date ?: 0L).year.year //.unOffset()
        var dateEnd = DateTimeTz.fromUnixLocal(statik.lastOrNull()?.date ?: 0L).year.year //.unOffset()

        if (dateEnd<dateStart) {
            val tmp = dateStart
            dateStart = dateEnd
            dateEnd = tmp
        }

        val listRez: MutableList<ItemYearGrafTwoRect> = mutableListOf()
        var sumyearRasx = 10.0
        var sumyearDox = 10.0
        for (year in dateStart..dateEnd) {
            sumyearRasx = statik.filter {
                DateTimeTz.fromUnixLocal(it.date).year.year == year //.unOffset()
            }.firstOrNull()?.sumyearrasx ?: 0.0
            sumyearDox = statik.filter {
                DateTimeTz.fromUnixLocal(it.date).year.year == year //.unOffset()
            }.firstOrNull()?.sumyeardox ?: 0.0
            listRez.add(
                ItemYearGrafTwoRect(
                    year,
                    statik.filter {
                        DateTimeTz.fromUnixLocal(it.date).year.year == year //.unOffset()
                    }.toMutableList().apply {
                        for (i in 1..12) {
                            if (this.find { DateTimeTz.fromUnixLocal(it.date).month1 == i } == null) {
                                this.add(
                                    ItemTwoRectDiagWithDate(
                                        year.toString(),
                                        i.toString(),
                                        "$year-$i",
                                        DateTimeTz.utc(DateTime(year,i,1),DateTimeTz.nowLocal().offset).localUnix(),
                                        0.0,
                                        0.0,
                                        sumyearDox,
                                        sumyearRasx,
                                        0.0,
                                        0.0
                                    )
                                )
                            }
                        }
                    }.sortedByDescending { it.date }.map {
                        ItemTwoRectDiag(
                            it.year,
                            it.month,
                            it.monthyear,
                            it.summadox,
                            it.summarasx,
                            it.sumyeardox,
                            it.sumyearrasx,
                            it.procentdox,
                            it.procentrasx
                        )
                    }
                )
            )
        }
        updF(listRez)
    } else {
        val year = DateTimeTz.nowLocal().year.year.toString()
        updF(
            mutableListOf(
                ItemYearGrafTwoRect(
                    year.toInt(), listOf(
                        ItemTwoRectDiag(year, "1","$year-1", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "2","$year-2", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "3","$year-3", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "4","$year-4", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "5","$year-5", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "6","$year-6", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "7","$year-7", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "8","$year-8", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "9","$year-9", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "10","$year-10", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "11","$year-11", 0.0, 0.0, 0.0, 0.0, 0.0,0.0),
                        ItemTwoRectDiag(year, "12","$year-12", 0.0, 0.0, 0.0, 0.0, 0.0,0.0)
                    )
                )
            )
        )
    }
}
