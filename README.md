# Itreumme (Demo repository)

<picture>
  <img width="450px" alt="Logo" src="https://FitConstructorImg.b-cdn.net/other/itreumme.png">
</picture>

![Static Badge](https://img.shields.io/badge/made_by-Ragefalcon-orange)

## Особенности репозитория
Itreumme мой личный творческий проект и по совместительству песочница 
в которой я осваиваю новые технологии. С основными целями и идеями проекта 
вы можете ознакомиться на <a  href="https://itreum.me">основном сайте</a>.
Там же можно скачать ознакомительные версии для PC, MacOS и Android. 
> Версии для desktop на данный момент более функциональны, в версии для Android
> пока что реализовано меньше функционала.

Данный репозиторий предназначен для ознакомления с технической стороной проекта.
Т.к. проект писался параллельно с изучением используемых технологий и одним человеком в творческом формате,
при детальном изучении можно найти код разного качества как с точки зрения надежности, 
так и с точки зрения форматирования... :-] Проект уже относительно большой, если бы 
не общая идея, то его можно было бы разбить на несколько отдельных приложений, и к сожалению
сейчас у меня нет возможности полностью его отредактировать и оформить так, чтобы 
он везде выглядел как надо, и можно было легко попробовать протестировать различные его 
модули. Тем не менее ниже я попробую в общих чертах описать структуру проекта и 
используемые технологии, а так же дать ссылки на участки, которые могут показаться
интересными с той или иной стороны.

## Структура проекта и технологии

Краткий стек:
- Kotlin
- Kotlin Multiplatform
- SQLDelight
- XML, View binding
- Compose for Desktop
- Ktor
- Google Drive


Разделение по модулям:
- <b>common</b> - общий модуль который используется остальными. В нем содержится вся 
бизнес-логика, взаимодействие с базой данных, а также взаимодействие с Google Drive 
после авторизации пользователя. 
- <b>android</b> - реализация интерфейса для Android и авторизация в Google Drive.
- <b>desktop</b> - реализация интерфейса для Windows и MacOS и авторизация 
для них в Google Drive. Этот модуль без всяких изменений позволяет скомпилировать 
версии программы как для Windows, так и для MacOS.
 
В качестве архитектуры используется MVVM.

### База данных

В общем модуле формируется группа объектов, которые подписываются на обновления базы данных.
Получаемые данные записываются в объекты определенного типа, 
который с помощью <b>Kotlin Multiplatform</b> имеет отдельные реализации для Android 
и для Desktop версии. Для каждой платформы на выходе выдаются соответствующие 
наблюдаемые типы данных(LiveData для Android и State для Compose for Desktop).
Таким образом для каждой платформу можно напрямую взять данные для отображения из 
общего модуля. Подробнее с этим можно ознакомиться 
<a href="">[здесь](/common/src/commonMain/kotlin/ru/ragefalcon/sharedcode/viewmodels/UniAdapters/).

Также в общем модуле соответственно располагаются все функции по добавлению и 
изменению записей в базе данных, а так же по настройке того какие именно данные
запрашиваются.

С SQL запросами к базе данных можно ознакомиться 
[здесь](/common/src/commonMain/sqldelight/ru/ragefalcon/sharedcode/)(без описания).
С учетом функционала приложения, которое предназначено для сбора и анализа относительно
большого количества разнообразных данных, там можно встретить запросы самой разной
сложности, с рекурсиями и триггерами. К сожалению их форматирование зачастую может 
выглядеть не очень хорошо.

### Android
<table>
  <tr>
    <td>
      <img width="200px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_07.jpg" alt="Open Training Demo">
    </td>
    <td>
      <img width="200px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_08.jpg" alt="Change Avatar Demo">
    </td>
    <td>
      <img width="200px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_09.jpg" alt="Change Avatar Demo">
    </td>
  </tr>
</table>

Проект начинался с реализации для Android и это было достаточно давно, на тот момент
Jetpack Compose еще только подходил к релизу, поэтому реализация была сделана на XML.
В какой-то момент я полностью переключился на desktop версию с Compose for Desktop,
с расчетом на то, что после можно будет переиспользовать/открыть этот код для Android.
На данный момент это еще не реализовано, но я планирую сделать это в будущем.

Основной интерфейс реализован с использованием одной Activity, 
фрагментов и NavController. 

В интерфейсе используются различные переходы и анимации.

Некоторые решения, которые могут показаться интересными:
- [универсальные айтемы и адаптер для RecyclerView](/android/main/java/ru/ragefalcon/tutatores/adapter/unirvadapter/)
- [класс фрагментов, который упрощает работу с ними](/android/main/java/ru/ragefalcon/tutatores/commonfragments/)
- [различные Custom View для отображения диаграмм, графиков и др.](/android/main/java/ru/ragefalcon/tutatores/ui/)

### Desktop
<table>
  <tr>
    <td>
      <img width="250px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_01.jpg" alt="Open Training Demo">
    </td>
    <td>
      <img width="250px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_02.jpg" alt="Change Avatar Demo">
    </td>
    <td>
      <img width="250px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_03.jpg" alt="Change Avatar Demo">
    </td>
  </tr>
  <tr>
    <td>
      <img width="250px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_04.jpg" alt="Open Training Demo">
    </td>
    <td>
      <img width="250px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_05.jpg" alt="Change Avatar Demo">
    </td>
    <td>
      <img width="250px" src="https://fitconstructorimg.b-cdn.net/other/Itreumme/Screenshot_Itreumme_06.jpg" alt="Change Avatar Demo">
    </td>
  </tr>
</table>

Версия для Desktop написана с помощью Compose for Desktop, который фактически
является Jetpack Compose для настольных компьютеров. Этот модуль реализует достаточно
большой функционал для пользовательского интерфейса, но Compose уже сам по себе
делает код проще и читабельнее, так что выделить на этом фоне какие-то необычные
решения сложно. Приходилось время от времени решать проблемы связанные с desktop 
элементами и функционалом, которые по умолчанию не предусмотрены в Jetpack Compose,
а в Compose for Desktop еще полноценно не реализованы, но думаю часть из них
не очень интересны, а часть возможно уже решена в новых версиях, т.к. платформа
активно развивается.

Диаграммы и графики также как в версии для Android рисовались вручную на Canvas.

Так же используются анимации. Можно, наверное, отметить подход для реализации
растягивания одного элемента из списка при его выборе/активации, но думаю с ним 
лучше ознакомиться в другом репозитории, где данный подход реализован уже напрямую в 
Jetpack Compose (посмотреть можно 
<a  href="https://github.com/Ragefalcon/MasterGYM_android/blob/master/app/src/main/java/ru/ragefalcon/mastergym_android/view/elements/CommonOpenItemPanel.kt">здесь</a>). 

Для стилизации приложения я написал встроенный редактор стилей, он позволяет в уже 
запущенной программе отредактировать стиль практически каждого элемента, при этом, 
как мне кажется, это удалось реализовать относительно небольшим количеством кода, 
для такой задачи. Подробнее можно посмотреть 
[здесь](/common/src/commonMain/kotlin/ru/ragefalcon/sharedcode/viewmodels/MainViewModels/Interface).

### iOS

Также в самом начале я тестировал совместимость общего модуля с iOS. Эксперимент
был удачный, я реализовал несколько экранов на SwiftUI и смог получать и изменять 
данные в базе данных. С тех пор поддержка Kotlin Multiplatform на iOS только 
улучшалась и даже началась разработка Compose for iOS. Так что большую часть кода
данного проекта можно переиспользовать и для iOS.

## Запуск проекта из репозитория

Т.к. данный репозиторий собирался в ознакомительных целях, то полноценно запуск из
этого репозитория я не тестировал и не готовил.

Desktop версия должна запуститься:

- `./gradlew run` - run application

Android версия в данном репозитории рассогласованна с общим модулем и поэтому не 
запустится.