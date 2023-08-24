# Класс фрагментов, который упрощает работу с ними

Для объявления фрагментов требуется много шаблонного кода, так же много шаблонного 
кода требуется для сохранения состояния при пересборке фрагмента. Я решил попробовать
упаковать часть функций внутрь одного класса от которого можно было бы наследовать
фрагменты и значительно уменьшать код необходимый для этого.

Начнем с сохранения состояния. Я наткнулся на 
<a href="https://habr.com/ru/post/336994/">статью на Хабр</a> в которой говорилось
о том как можно применить делегаты из Kotlin для упрощения сохранения состояния.
Там используя делегаты при изменении переменной она сразу сохранялась в Bundle, а 
при чтении сверялась с кэшем и при необходимости так же обращалась к Bundle. 
Bundle объект сохранялся внутри фрагмента.
Мне понравилась эта идея и я подумал что было бы здорово инкапсулировать этот механизм
прямо в класс фрагмента, так появился класс `FragSaveInstanseDelegate` его можно 
найти в этой папке в файле с одноименным названием. Там реализован механизм описанный
в статье с небольшими дополнениями, только теперь это внутри отдельного класса
с перезаписанными `onCreate` и `onSaveInstanceState` от которого можно наследовать
фрагменты. Если внутри наследника такого класса объявить переменную следующим образом:
`var selItem: ItemIdea? by instanceState(startValue)`, то она автоматически будет 
сохранять свое состояние. `instanceStateDef` позволяет так же указывать значение по 
умолчанию.


Продолжим упрощать объявление фрагмента. Можно легко спрятать установку связи
фрагмента с его xml:
```kotlin
open class BaseFragment<T : ViewBinding>(
    private val inflateBF: (LayoutInflater, ViewGroup?, Boolean) -> T
) : FragSaveInstanseDelegate() {
    private var _binding: T? = null
    protected val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBF(inflater, container, false)
        return binding.root
    }
}
```

А если во многих фрагментах вызывается ViewModel, то можно включить и этот момент: 
```kotlin
open class BaseFragmentVM<T : ViewBinding>(
    inflateBF: (LayoutInflater, ViewGroup?, Boolean) -> T
) : BaseFragment<T>(inflateBF) {
    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }
}
```

Фрагменты объявленные таким образом будут автоматически привязываться через
View Binding, иметь уже объявленные ViewModel, а переменные объявленные с помощью
`instanceState` и `instanceStateDef` будут автоматически сохранять свое состояние.
```kotlin
class AnyFragment() : BaseFragmentVM<AnyFragmentXMLname_Binding>(
    AnyFragmentXMLname_Binding::inflate
) {
    var varWithSaveInstanceState: AnyTypeForBundle? by instanceState(startValue)
}
```


---
[На главную](/)
