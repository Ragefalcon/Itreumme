package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemYearGraf(
    val year: Int,
    val month: List<ItemRectDiag>
) : Id_class(id_main = year.toString()), Parcelable

@Parcelize
class ItemYearGrafTwoRect(
    val year: Int,
    val month: List<ItemTwoRectDiag>
) : Id_class(id_main = year.toString()), Parcelable
