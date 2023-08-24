# Различные Custom View для отображения диаграмм, графиков и др.

В папке drawclasses можно найти примеры различных экспериментов с Custom View, 
визуальная часть которых рисуется на Canvas. Некоторые из них я бы сейчас написал
по другому. 

Для ознакомления можно посмотреть вспомогательные класс 
<a  href="ru/ragefalcon/tutatores/ui/drawclasses/DrawViewHelper.kt">DrawViewHelper</a> и его
наследников, таких как <a  href="ru/ragefalcon/tutatores/ui/drawclasses/EffektShkalDrawHelper.kt">EffektShkalDrawHelper</a>
и <a  href="ru/ragefalcon/tutatores/ui/drawclasses/RectDiagramDraw.kt">RectDiagramDraw</a>.

В папке element_fragment несколько классов наследующих другие View.

<a  href="ru/ragefalcon/tutatores/ui/element_fragment/VyborStatFragment.kt">VyborStatFragment</a> 
наследуется от `FrameLayout`.

<a  href="ru/ragefalcon/tutatores/ui/element_fragment/MyDateSelect.kt">MyDateSelect</a>
и <a  href="ru/ragefalcon/tutatores/ui/element_fragment/MyTimeSelect.kt">MyTimeSelect</a>
наследуется от `TextView`.
