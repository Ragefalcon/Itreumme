package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.ComplexOpisVMobjForSpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.JournalVMobjForSpis

class JournalVMfun(private val mDB: Database, private val spisVM: JournalVMobjForSpis, private val spisCO: ComplexOpisVMobjForSpis) {
    fun setBloknotForSpisIdea(idBlok: Long, sortField: String = "name"){
        spisVM.spisIdea.updateQuery(mDB.spisIdeaQueries.selectIdeaInBloknot(idblok = idBlok, sortField = sortField))
        spisCO.spisComplexOpisForIdea.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("spis_idea", idBlok),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("spis_idea", idBlok),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("spis_idea", idBlok),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("spis_idea", idBlok),
        )
    }
    fun setBloknotForSpisIdeaForSelect(idBlok: Long, array_iskl: Collection<Long> = listOf(), sortField: String = "name"){
        spisVM.spisIdeaForSelect.updateQuery(mDB.spisIdeaQueries.selectIdeaInBloknotForSelect(idblok = idBlok, iskl = array_iskl, sortField = sortField))
    }
    fun setIdeaForSpisStapIdea(idIdea: Long, searchStr: String = "", sortField: String = "name"){
//        spisVM.spisIdeaStap.updateQuery(mDB.spisStapIdeaQueries.selectIdeaStapInIdea(idIdea = idIdea, searchStr = searchStr, sortField = sortField))
        if (searchStr == "" || searchStr == "*") {
            spisVM.spisIdeaStap.updateQuery(
                mDB.spisStapIdeaQueries.selectIdeaStapInIdea(
                    idIdea = idIdea,
                    sortField = sortField
                )
            )
            spisCO.spisComplexOpisForIdeaStap.updateQuery(
                mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("spis_stap_idea", idIdea),
                mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("spis_stap_idea", idIdea),
                mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("spis_stap_idea", idIdea),
                mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("spis_stap_idea", idIdea),
            )
        }   else {
            spisVM.spisIdeaStap.updateQuery(
                mDB.spisStapIdeaQueries.selectIdeaStapInIdeaWithSearch(
                    idIdea = idIdea,
                    searchStr = "${searchStr.uppercase()}*",
                    sortField = sortField
                )
            )
        }
    }
}