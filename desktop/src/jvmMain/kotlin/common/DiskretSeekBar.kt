package common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.ButtonSeekBarStyleState
import extensions.getComposable
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import viewmodel.MainDB
import viewmodel.StateVM
import kotlin.reflect.KClass

@Composable
fun comSeekTab(
    tab: MyTabsForSeek,
    modifier: Modifier,
    startTop: Boolean = false,
    endTop: Boolean = false,
    startBottom: Boolean = startTop,
    endBottom: Boolean = endTop,
    vertical: Boolean = false
) {
    fun radius(bb: Boolean) = if (bb) 10.dp else 0.dp
    Surface(
        modifier = modifier, shape = RoundedCornerShape(
            topStart = radius(startTop),
            bottomStart = radius(startBottom),
            topEnd = radius(endTop),
            bottomEnd = radius(endBottom)
        ), color = if (tab.isActive) {
            Color(0xFF2B2B2B)
        } else {
            Color(0xFF464D45)
        }
    ) {
        Row(
            Modifier.clickable(remember(::MutableInteractionSource), indication = null) {
                tab.activate()
            }.border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(Color(0xFF888888), Color(0xFF888888))),
                shape = RoundedCornerShape(
                    topStart = radius(startTop),
                    bottomStart = radius(startBottom),
                    topEnd = radius(endTop),
                    bottomEnd = radius(endBottom)
                )
            ).padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = tab.name,
                color = Color(0xFFFFF7D9),
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }

}


@Composable
fun <T : tabElement> comEnumSeekTab(
    tab: MyEnumTabsForSeek<T>,
    modifier: Modifier,
    start: Boolean = false,
    end: Boolean = false,
    myStyleTextButton: ButtonSeekBarStyleState? = null,
    vertical: Boolean = false,
    thin: Boolean = false
) {
    with(myStyleTextButton ?: StateVM.commonButtonSeekBarStyleState.value) {
        val shapeCard = if (vertical) {
            if (start && end) shapeCardIndiv else if (start) shapeCardBottom else if (end) shapeCardTop else shapeCardCenter
        } else {
            if (start && end) shapeCardIndiv else if (start) shapeCardStart else if (end) shapeCardEnd else shapeCardCenter
        }

        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(blurRadius = getElevation().elevation(true, interactionSource).value.toPx()),
                modifier
            ) {
                Box(
                    modifier
                        .run {
                            if (vertical) this.fillMaxHeight() else this.fillMaxWidth()
                        }
                        .border(BorderStroke(borderWidth, if (tab.isActive) borderActive else border), shapeCard)
                        .background(if (tab.isActive) backgroundActive else background, shapeCard)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { tab.activate() },
                    contentAlignment = if (vertical) Alignment.BottomCenter else Alignment.Center
                ) {
                    Row(
                        Modifier.run {
                            if (vertical) this.width(100.dp) else this
                        }.padding(if (thin) 2.dp else 10.dp),
                        verticalAlignment = if (vertical) Alignment.Bottom else Alignment.CenterVertically,
                    ) {
                        Text(
                            tab.elem.nameTab, modifier = Modifier.offset(
                                if (isHovered) offsetTextHover.x.dp else 0.dp,
                                if (isHovered) offsetTextHover.y.dp else 0.dp
                            ).padding(0.dp), style = if (isHovered) textStyleShadowHover else textStyle
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun comSeekTabVert(
    tab: MyTabsForSeek, modifier: Modifier, start: Boolean = false, end: Boolean = false, vertical: Boolean = false
) {

    fun radius(bb: Boolean) = 10.dp
    Surface(
        modifier = modifier, shape = RoundedCornerShape(
            topStart = radius(start), bottomStart = radius(start), topEnd = radius(end), bottomEnd = radius(end)
        ), color = if (tab.isActive) {
            Color(0xFF2B2B2B)
        } else {
            Color(0xFF464D45)
        }
    ) {
        Row(
            Modifier.width(100.dp).clickable(remember(::MutableInteractionSource), indication = null) {
                tab.activate()
            }.border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(Color(0xFF888888), Color(0xFF888888))),
                shape = RoundedCornerShape(
                    topStart = radius(start),
                    bottomStart = radius(start),
                    topEnd = radius(end),
                    bottomEnd = radius(end)
                )
            ).padding(4.dp),
            verticalAlignment = Alignment.Bottom,
        ) {

            Text(
                text = tab.name,
                color = Color(0xFFFFF7D9),
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp),
                maxLines = 1,
                textAlign = TextAlign.Start
            )

        }
    }

}

inline fun <reified T : Enum<T>> iterator(): Iterator<T> = enumValues<T>().iterator()


/**
 * Пример класса для использования
 * enum class enumClassForTab( override val nameTab: String) : tabElement {
 *     tab1("tab1Name"),
 *     tab2("tab2Name");
 * }
 *
 * Для использования EnumDiskretSeekBar(enumClassForTab::class)
 *
 * Чтобы не прошел класс ниже, нужно указывать where T : Enum<T>, T : tabElement
 * class enumClassForTab2( override val nameTab: String) : tabElement {
 * }
 *
 * Если вместо KClass<T> будет Class<T>, то вместо *tabs.java.enumConstants
 * будет  *tabs.enumConstants, но передавать нужно будет enumClassForTab::class.java
 *
 ***/
class EnumDiskretSeekBar<T>(
    var tabs: KClass<T>,
    private var selected: T = listOf(*tabs.java.enumConstants).first(),
    val vertical: Boolean = false,
    val listenerSeekBar: ((T) -> Unit)? = null
) where T : Enum<T>, T : tabElement {

    private val selection = SingleSelectionType<T>()

    private val ttabs = listOf(*tabs.java.enumConstants).map {
        MyEnumTabsForSeek(it, selection) {
            if (it.elem != selected) {
                listenerSeekBar?.invoke(it.elem)
                selected = it.elem

            }
        }.apply {
            selected?.let {
                if (selected == this.elem) {
                    activate()

                    listenerSeekBar?.invoke(this.elem)
                }
            }
        }
    }
    val active: T get() = selection.selected ?: selected

    fun setSelection(elem: T) {
        ttabs.find { it.elem == elem }?.let {
            if (selected != elem) {
                selected = elem
                it.activate()

                listenerSeekBar?.invoke(it.elem)
            }
        }
    }

    @Composable
    fun show(
        modifier: Modifier = Modifier,
        style: CommonInterfaceSetting.MySettings.ButtonSeekBarStyleItemSetting = MainDB.styleParam.commonParam.mainSeekBarStyle,
        thin: Boolean = false
    ) {
        style.getComposable(::ButtonSeekBarStyleState) { styleButt ->
            if (vertical) {
                Column(modifier) {
                    var i = ttabs.size
                    for (tab in ttabs) {
                        comEnumSeekTab(
                            tab, Modifier.weight(1f).wrapContentWidth(), i == 1, i == ttabs.size, styleButt, true, thin
                        )
                        i--
                    }
                }
            } else {
                Row(modifier) {
                    var i = 1
                    for (tab in ttabs) {
                        comEnumSeekTab(
                            tab, Modifier.weight(1f), i == 1, i == ttabs.size, styleButt, thin = thin
                        )
                        i++
                    }
                }
            }
        }
    }
}

class DiskretSeekBar(
    val tabs: List<Pair<String, String>>,
    var codActivated: String = tabs.firstOrNull()?.second ?: "",
    val vertical: Boolean = false,
    val listenerSeekBar: ((MyTabsForSeek) -> Unit)? = null
) {

    private val selection = SingleSelection()
    private val ttabs = tabs.map {
        MyTabsForSeek(it.first, it.second, selection) {
            if (it.cod != codActivated) {
                listenerSeekBar?.invoke(it)
                codActivated = it.cod

            }
        }.apply {
            codActivated?.let {
                if (codActivated == this.cod) {
                    activate()

                    listenerSeekBar?.invoke(this)
                }
            }
        }
    }
    val active: MyTabsForSeek? get() = selection.selected as MyTabsForSeek?

    fun setSelection(cod: String) {
        ttabs.find { it.cod == cod }?.let {
            if (codActivated != cod) {
                codActivated = cod
                it.activate()

                listenerSeekBar?.invoke(it)
            }
        }
    }

    @Composable
    fun show(modifier: Modifier = Modifier) {
        if (vertical) {
            Column(modifier) {
                var i = ttabs.size
                for (tab in ttabs) {
                    comSeekTabVert(tab, Modifier.weight(1f).wrapContentWidth(), i == 1, i == tabs.size)
                    i--
                }
            }
        } else {
            Row(modifier) {
                var i = 1
                for (tab in ttabs) {
                    comSeekTab(tab, Modifier.weight(1f), i == 1, i == tabs.size)
                    i++
                }
            }
        }
    }
}

class DiskretSeekBarManyRows(
    val tabs: List<List<Pair<String, String>>>,
    val codActStart: String? = null,
    val vertical: Boolean = false,
    val listener: ((MyTabsForSeek) -> Unit)? = null
) {

    constructor(
        tabs: List<Pair<String, String>>,
        countInRow: Int,
        codActStart: String? = null,
        vertical: Boolean = false,
        listener: ((MyTabsForSeek) -> Unit)? = null
    ) : this(
        mutableListOf<List<Pair<String, String>>>().apply {
            val tmpList: MutableList<Pair<String, String>> = mutableListOf()
            var iii = 0
            tabs.forEach { pairTab ->
                iii++
                if (iii == countInRow) {
                    tmpList.add(pairTab)
                    this.add(mutableListOf<Pair<String, String>>().apply { addAll(tmpList) })
                    tmpList.clear()
                    iii = 0
                } else {
                    tmpList.add(pairTab)
                }
            }
            if (iii != 0) {
                this.add(mutableListOf<Pair<String, String>>().apply { addAll(tmpList) })
            }
        }, codActStart, vertical, listener
    )

    private val selection = SingleSelection()
    private val tabsList = tabs.map { ttabsItem ->
        ttabsItem.map {
            MyTabsForSeek(it.first, it.second, selection).apply {
                codActStart?.let {
                    if (it == this.cod) activate()
                }
            }
        }
    }
    private val ttabs: MutableList<MyTabsForSeek> = mutableListOf<MyTabsForSeek>().apply {
        tabsList.forEach { list ->
            this.addAll(list)
        }
    }
    val active: MyTabsForSeek? get() = selection.selected as MyTabsForSeek?

    fun setSelection(cod: String) {
        ttabs.find { it.cod == cod }?.let {
            it.activate()
        }
    }

    @Composable
    private fun startListener(tb: MyTabsForSeek?) {
        listener?.let {
            tb?.let(it)
        }
    }

    @Composable
    fun show(modifier: Modifier = Modifier) {
        if (vertical) {
            Row(modifier) {
                tabsList.forEach { oneRow ->
                    Column {
                        var i = oneRow.size
                        for (tab in oneRow) {
                            comSeekTabVert(tab, Modifier.weight(1f).wrapContentWidth(), i == 1, i == tabs.size)
                            i--
                        }
                    }
                }
            }
        } else {
            Column(modifier) {
                var j = 1
                tabsList.forEach { oneRow ->
                    Row {
                        var i = 1
                        for (tab in oneRow) {
                            comSeekTab(
                                tab,
                                Modifier.weight(1f),
                                i == 1 && j == 1,
                                i == oneRow.size && j == 1,
                                i == 1 && j == tabsList.size,
                                i == oneRow.size && j == tabsList.size
                            )
                            i++
                        }
                    }
                    j++
                }
            }
        }
        startListener(active)
    }
}

class MyTabsForSeek(
    val name: String, val cod: String, var selection: SingleSelection, val listener: ((MyTabsForSeek) -> Unit)? = null
) {
    val isActive: Boolean
        get() = selection.selected === this

    fun activate() {
        selection.selected = this
        listener?.invoke(this)
    }
}

class MyEnumTabsForSeek<T : tabElement>(
    val elem: T, var selection: SingleSelectionType<T>, val listener: ((MyEnumTabsForSeek<T>) -> Unit)? = null
) {
    val isActive: Boolean
        get() = selection.selected === this.elem

    fun activate() {
        selection.selected = this.elem
        listener?.invoke(this)
    }
}