# Универсальные айтемы и адаптер для RecyclerView

RecyclerView часто является одним из основных элементов реализующих функционал приложения.
При этом его использование затруднено достаточно большим количеством шаблонного кода,
который нужно написать, чтобы связать типы данных для отображения с View элементами.
Посмотрев в интернете кто как решает эту проблему, я вдохновился и решил написать
универсальный адаптер который бы вообще исключил привязку к типу, а вся информация 
о типах и View получалась бы из универсального класса айтема. В результате я написал 
класс `UniRVAdapter` использующий в качестве ViewHolder класс `UniRVMainAdapterViewHolder`
и `UniRVItem` в качестве айтемов.
> Реализация использует View binding

Реализация класса `UniRVAdapter` находится в файле `UniRVAdapter.kt`, а реализация классов
`UniRVMainAdapterViewHolder`и `UniRVItem`, а так же других вспомогательных классов и 
функций - в файле `UniRVItem.kt`.

Вся информация о View и данные будут получаться из класса `UniRVItem`, который 
принимает на вход объект вспомогательного класса `BaseUniRVItem<*>`. В конечном 
итоге для различных типов айтемов нужно будет реализовать наследников
именно класса `BaseUniRVItem<*>`.

```Kotlin
open class BaseUniRVItem<T : Id_class>(
    private val data: T,
    private val holder: UniRVViewHolder<T>,
) {
    ...
}
```
Наследование от `Id_class` гарантирует наличие id у айтема. При этом сравнение айтемов
идет не только по id, но и по типу, так что в списке могут быть айтемы с одинаковыми 
id, но с разными типами.
```Kotlin
@Parcelize
open class Id_class (
    val id_main: String
):Parcelable
```


С учетом всех вспомогательных функций и классов код для создания произвольного типа айтемов
будет выглядеть следующим образом:
```Kotlin
class AnyUriRVItem(
    data: AnyItemIdClass,
    ...
) : BaseUniRVItem<ItemDoxod>(
    data,
    getUniRVViewHolder(NameFromXMLFile_Binding::inflate) { vh, item, rvset ->
        if (vh.binding is NameFromXMLFile_Binding) {
            with(vh.binding) {
                ...
            }
        }
    }
)
```

Ниже пример конкретного айтема из проекта:
```Kotlin
class DoxodRVItem(
    data: ItemDoxod,
    /**
     *  Аргументы selectListener,tapListener,longTapListener уже не относятся 
     *  к общей реализации и являются особенностью конкретно данного класса
     * */
    selectListener: ((ItemDoxod) -> Unit)? = null,
    tapListener: ((ItemDoxod) -> Unit)? = null,
    longTapListener: ((ItemDoxod) -> Unit)? = null
) : BaseUniRVItem<ItemDoxod>(
    data,
    /**
     *  ItemDoxodBinding генерируется из item_doxod.xml с помощью View Binding
     * */
    getUniRVViewHolder(ItemDoxodBinding::inflate) { vh, item, rvset ->
        /**
         *  Код отсюда будет выполняться внутри
         *  override fun onBindViewHolder(holder: UniRVMainAdapterViewHolder, position: Int)
         *  в UniRVAdapter
         * */
        if (vh.binding is ItemDoxodBinding) {
            with(vh.binding) {

                /**
                 * Ниже заполнение полей данными из дата-класса с помощью binding
                 * */
                textName.text = item.name
                textType.text = item.type
                textSchet.text = item.schet
                textSumma.text = item.summa.toString()
                textData.text = Date(item.data).format("dd MMM yyyy")

                
                /**
                 *  Ниже реализация выделения и взаимодействия с айтемом.
                 *  Возможность выделять айтем встроена в UniRVAdapter,
                 *  но чтобы ею воспользоваться нужен код ниже с использованием rvset.
                 * */
                if (vh.itemView.isSelected) {
                    selectListener?.invoke(item)
                }
                vh.itemView.setOnClickListener { 
                    vh.bindItem?.let { rvset.selFunc(it) }
                    tapListener?.invoke(item)
                }
                vh.itemView.setOnLongClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    longTapListener?.invoke(item)
                    true
                }
            }
        }
    }
)
```

Дальше посмотрим как использовать этот код для заполнения RecyclerView внутри Fragment:
```kotlin
    ...
    
    private var rvmAdapter = UniRVAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ...
            /**
             *  rvFinanceList: RecyclerView
             */
            with(rvFinanceList) {
                adapter = rvmAdapter
                layoutManager = LinearLayoutManager(context)
            }
            ...
            
            /**
             *  listItemDoxod: List<ItemDoxod>  
             *  formUniRVItemList - вспомогательная функция из UniRVItem.kt
             */
            rvmAdapter.updateData(formUniRVItemList(listItemDoxod) { item ->
                DoxodRVItem(
                    item,
                    selectListener = { ... },
                    tapListener = { ... },
                    longTapListener = { ... }
                )
            })
            ...
        }
        ...
    }
```

Чтобы объединить в одном списке различные типы айтемов нужно вручную сформировать список
из `UniRVItem( принимающих на вход различные типы BaseUniRVItem )` и подать его на в `rvmAdapter.updateData()`.


---
[На главную](/)
