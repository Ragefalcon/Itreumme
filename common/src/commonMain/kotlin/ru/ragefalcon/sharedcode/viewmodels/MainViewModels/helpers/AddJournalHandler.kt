package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.longMinusTimeLocal
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis

class AddJournalHandler(
    private var mdb: Database,
    private val addComplexOpis: AddComplexOpisHandler
) {
    fun addBloknot(
        name: String,
        opis: List<ItemComplexOpis>,
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis) {
        mdb.spisBloknotQueries.insertOrReplace(
            name = name,
            opis = "opis"
        )
    }

    fun updBloknot(
        name: String,
        opis: List<ItemComplexOpis>,
        id: Long
    ): List<PairId> {
        mdb.spisBloknotQueries.update(
            name = name,
            opis = "opis",
            id = id
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun delBloknot(
        id: Long,
        delImageComplexOpis: (Long)->Unit
    ) {
        mdb.spisBloknotQueries.delete(id = id)
        delImageComplexOpis(id)
    }

    fun addIdea(
        name: String,
        opis: List<ItemComplexOpis>,
        data: Long,
        stat: Long,
        parent_id: Long,
        bloknot: Long
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis) {
        mdb.spisIdeaQueries.insertOrReplace(
            name = name,
            opis = "opis",
            data_ = data.withOffset().longMinusTimeLocal(),
            stat = stat,
            parent_id = parent_id,
            bloknot = bloknot
        )
    }

    fun updIdea(
        id: Long,
        name: String,
        opis: List<ItemComplexOpis>,
        data: Long,
        stat: Long,
        parent_id: Long,
        bloknot: Long
    ): List<PairId> {
        mdb.spisIdeaQueries.update(
            id = id,
            name = name,
            opis = "opis",
            data = data.withOffset().longMinusTimeLocal(),
            stat = stat,
            parent_id = parent_id,
            bloknot = bloknot
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun delIdea(
        id: Long,
        delImageComplexOpis: (Long)->Unit
    ) {
        mdb.spisIdeaQueries.delete(id = id)
        delImageComplexOpis(id)
    }

    fun addStapIdea(
        name: String,
        opis: List<ItemComplexOpis>,
        idea_id: Long,
        data: Long,
        stat: Long
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis, withId = { docid ->
        mdb.spisStapIdeaQueries.insertOrReplaceFTS(
            docid = docid,
            name_fts = name.uppercase(),
            opis_fts = "OPIS"
        )
    }) {
        mdb.spisStapIdeaQueries.insertOrReplace(
            name = name,
            opis = "opis",
            idea_id = idea_id,
            data_ = data.withOffset().longMinusTimeLocal(),
            stat = stat
        )
    }

    fun updStapIdea(
        id: Long,
        name: String,
        opis: List<ItemComplexOpis>,
        idea_id: Long,
        data: Long,
        stat: Long
    ): List<PairId> {
        mdb.spisStapIdeaQueries.update(
            id = id,
            name = name,
            opis = "opis",
            idea_id = idea_id,
            data = data.withOffset().longMinusTimeLocal(),
            stat = stat,
            name_fts = name.uppercase(),
            opis_fts = "OPIS"
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun delStapIdea(
        id: Long,
        delImageComplexOpis: (Long)->Unit
    ) {
        mdb.spisStapIdeaQueries.delete(id = id)
        delImageComplexOpis(id)
    }
}