package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.Journal.SelectBloknot
import ru.ragefalcon.sharedcode.Journal.SelectIdeaInBloknot
import ru.ragefalcon.sharedcode.Journal.SelectIdeaInBloknotForSelect
import ru.ragefalcon.sharedcode.Journal.Spis_stap_idea
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniConvertQueryAdapter

class JournalVMobjForSpis(val mDB: Database) {

    val spisBloknot = UniConvertQueryAdapter<SelectBloknot, ItemBloknot>() {
        ItemBloknot(
            id = it._id.toString(),
            name = it.name,
            opis = it.opis,
            countidea = it.countidea
        )
    }.apply {
        this.updateQuery(mDB.spisBloknotQueries.selectBloknot())
    }

    val spisIdea = UniConvertQueryAdapter<SelectIdeaInBloknot, ItemIdea>() {
        ItemIdea(
            level = it.level,
            id = it._id.toString(),
            name = it.name,
            data = it.data_,
            opis = it.opis,
            stat = it.stat,
            parent_id = it.parent_id,
            bloknot = it.bloknot,
            podstapcount = it.stapcount
        )
    }.apply {
        this.updateQuery(mDB.spisIdeaQueries.selectIdeaInBloknot(idblok = -1, sortField = "name"))
    }

    val spisIdeaStap = UniConvertQueryAdapter<Spis_stap_idea, ItemIdeaStap>() {
        ItemIdeaStap(
            id = it._id.toString(),
            name = it.name,
            data = it.data_,
            opis = it.opis,
            stat = it.stat,
            idea_id = it.idea_id
        )
    }.apply {
        this.updateQuery(mDB.spisStapIdeaQueries.selectIdeaStapInIdea(idIdea = -1, sortField = "name"))
    }

    val spisIdeaForSelect = UniConvertQueryAdapter<SelectIdeaInBloknotForSelect, ItemIdea>() {
        ItemIdea(
            level = it.level,
            id = it._id.toString(),
            name = it.name,
            data = it.data_,
            opis = it.opis,
            stat = it.stat,
            parent_id = it.parent_id,
            bloknot = it.bloknot,
            podstapcount = it.stapcount
//            svernut = !((it.svernut == "false")||(it.svernut == "False")),
        )
    }.apply {
        this.updateQuery(mDB.spisIdeaQueries.selectIdeaInBloknotForSelect(sortField = "name", idblok = -1,iskl = listOf()))
    }

}