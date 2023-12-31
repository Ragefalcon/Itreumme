# Редактор стилей

Данное решение скорее всего не очень подходит для конечной версии или как минимум 
требует оптимизация и уменьшения чувствительности к рефакторингу названий и 
изменению иерархии стилей. Но для внутреннего быстрого решения вполне может сгодиться.

Для того чтобы редактировать стиль приложения по средствам указания цветов, отступов
и других подобных параметров необходимо записать в базу данных огромное количество 
переменных. Можно их группировать и использовать сериализацию, но для этого нужно
будет написать много когда который просто описывает типы, а потом еще связать с 
получением из базы данных. Можно записывать базовые значения параметров, которые
можно свести к простым типам(Int, String, Double), но тогда нужно иметь для каждой
такой переменной уникальное имя и как то связывать их с переменными в програме.
Наверняка первый подход тоже можно было оптимизировать,
но я в итоге пошел по второму варианту, а для дальнейшей оптимизации возможно имеет
смысл их скомбинировать.

Я подумал, что с таким подходом все параметры 
стиля можно будет хранить в одной табличке, считать их одним запросом или записать
одной транзакцией. При этом было бы здорово чтобы в программе к этим переменным 
можно было бы обращаться через иерархию стиля 
(т.е. Объект_стиля.панель.айтем.кнопка.отступ). Я поискал возможное решение и наткнулся 
на следущий класс:
```kotlin
open class EnumObjectList<T>
private constructor( private val list: MutableList<T> ) :
    List<T> by list
{
    constructor() : this( mutableListOf() )

    protected fun <TAdd : T> add( item: TAdd ): TAdd =
        item.also { list.add( it ) }

    fun clearList() {
        list.clear()
    }
}
```

Сейчас бы я переделал этот вариант с List на Map. В качестве типа объектов для 
него был написан `sealed interface InterfaceSettingsType` который был предназначен 
для управления обновлениями параметров, а так же для получения из них наблюдаемых 
типов. 
Если где то внутри создается переменная типа `InterfaceSettingsType` и 
инициализируется через функцию `add` класса `EnumObjectList`, то она автоматом 
попадает в общий спискок. `val anyProperty = add(SomeInterfaceSettingsType(...))`.

В каждом `InterfaceSettingsType` находится уникальный код по которому свойство можно
записать в базу и считать оттуда. Чтобы сохранить уникальность имен в базе данных 
на вход `InterfaceSettingsType` подаются код для переменной и 
код раздела в котором находится свойство. Объект 
раздела `RazdelSetting` предназначен как раз для формирования иерархии стиля.
Помимо кода раздела в нем так же содержатся некоторые параметры для отображения
раздела в редакторе стиля. 

Теперь можно создать наследника класса раздела, 
например класс "Кнопка" и прописать в нем все свойства связанные со стилем кнопки.
После этого можно будет добавлять этот раздел уже в раздел "Айтем" и там автоматом 
будет создаваться уже набор свойств. Можно делать все эти классы открытыми и потом
расширять если нужно для конкретного случая добавить 1-2 свойства, при этом никакого
дополнительного взаимодействия с базой данных уже не требуется.

> [!WARNING]
> Мне хотелось еще упростить код и я добавил делегаты которые считывали имя
объявленной переменнойи использовали его в качестве кода для `InterfaceSettingsType`,
и тоже самое для создаваемого раздела. Теперь код выглядит совсем лаконично, но 
при этом такой подход порождает более важную проблему. Названия переменных теперь
связаны с кодами в базе данных и если переименовать переменную, то старые записи
в базе данных окажутся ни к чему не привязанными. То же самое будет если захочется
сменить иерархию свойств. Для моих экспериментальных задач, я решил, что мне подходит такое решение. 
Потому что это значительно упрощает код и на данном этапе у меня нет необходимости 
проводить миграцию от старых стилей к новым. В случае решения задач для конечного
пользователя стоит хорошо продумать возможные проблемы от такого решения.
 
 Код делегата для считывания имени переменной:
 ```kotlin
        inner class settName<T : InterfaceSettingsType>(private var sett: T) {

            operator fun provideDelegate(
                thisRef: Any?,
                property: KProperty<*>
            ): settNameDelegat<T> {
                sett.codeName = property.name
                return settNameDelegat(sett)
            }
        }

        inner class settNameDelegat<T : InterfaceSettingsType>(private var sett: T) {

            operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return sett
            }

            operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                value.codeName = property.name
                sett = value
            }
        }
```

Пример использования:
 ```kotlin
     val BRUSH_COLOR by settName(addColor("Цвет", mainColor))
```
`addColor` возвращает `InterfaceSettingsType`, значение "Цвет" передается для 
подписи значения в редакторе.

---


> Здесь я в общих чертах описал подход к решению задачи. В полном коде можно
найти большой набор вспомогательных классов и элементов. Я решил, что нет большого 
смысла в деталях расписывать их здесь, к тому же текущий код нужно как минимум 
переструктурировать. Но если будет потребность или желание разобраться подробнее
я могу помочь.


---

Чтобы посмотреть редактор в деле нужно запустить Desktop версию программы.
Для этого нужно или запустить проект из данного репозитория или скачать
подходящий установочный файл с <a href="https://itreum.me">сайта проекта</a>

В запущенной программе нужно запустить Настройки -> Помощь -> Редактор стилей.

Запустится еще одно окно с редактором.
Если элемент, свойства которого редактируют, в данный момент отображается, 
то он будет меняться следом за изменяемым параметром в реальном времени.

  <img width="500px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_10.jpg" alt="Change Avatar Demo">

---
[На главную](/)

