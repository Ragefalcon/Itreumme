# Различные Custom View для отображения диаграмм, графиков и др.

В папке drawclasses можно найти примеры различных экспериментов с Custom View, 
визуальная часть которых рисуется на Canvas. Некоторые из них я бы сейчас написал
по другому. 

Для ознакомления можно посмотреть вспомогательные класс
[DrawViewHelper](/android/src/main/java/ru/ragefalcon/tutatores/ui/drawclasses/DrawViewHelper.kt) и его
наследников, таких как
[EffektShkalDrawHelper](/android/src/main/java/ru/ragefalcon/tutatores/ui/drawclasses/EffektShkalDrawHelper.kt)
и
[RectDiagramDraw](/android/src/main/java/ru/ragefalcon/tutatores/ui/drawclasses/RectDiagramDraw.kt).

В папке element_fragment несколько классов наследующих другие View.

[VyborStatFragment](/android/src/main/java/ru/ragefalcon/tutatores/ui/element_fragment/VyborStatFragment.kt)
наследуется от `FrameLayout`.

[MyDateSelect](/android/src/main/java/ru/ragefalcon/tutatores/ui/element_fragment/MyDateSelect.kt)
и
[MyTimeSelect](/android/src/main/java/ru/ragefalcon/tutatores/ui/element_fragment/MyTimeSelect.kt)
наследуется от `TextView`.


---
[На главную](/)
