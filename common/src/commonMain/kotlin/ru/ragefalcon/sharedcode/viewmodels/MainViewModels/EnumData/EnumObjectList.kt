package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonObserveObj

/**
 *
 * https://whatheco.de/2020/05/05/list-of-strongly-typed-objects-acting-like-enum-in-kotlin/
 *
interface DataType { val typeName: String }

object GeolocationType : DataType
{
    override val typeName: String = "geolocation"

    fun create( longitude: Double, latitude: Double ) =
            Pair( longitude, latitude )
}

object SupportedTypes : EnumObjectList<DataType>()
{
val GEOLOCATION = add( GeolocationType )
}

val supportedTypeNames = SupportedTypes.map { it.typeName }

val data = SupportedTypes.GEOLOCATION.create( 42.0, 42.0 )

 */
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

open class EnumObject2List<T, R>
private constructor( private val list: MutableList<T>, val list2: MutableList<R> ) :
    List<T> by list
{
    constructor() : this( mutableListOf(), mutableListOf() )

    protected fun <TAdd : T> add( item: TAdd, ind: Int? = null ): TAdd =
        item.also { iitem -> ind?.let{list.add( it,iitem)} ?: list.add(iitem)}
    protected fun <RAdd : R> add2( item: RAdd, ind: Int? = null ): RAdd =
        item.also { iitem -> ind?.let{list2.add( it,iitem)} ?: list2.add(iitem)}
}

class BooleanItrComm
private constructor( startValue:Boolean,
    private val svernutPriv: CommonObserveObj<Boolean>,
    val itrCOO: ItrCommObserveObj<Boolean>  = ItrCommObserveObj(svernutPriv.getMyObserveObj()).apply { svernutPriv.setValue(startValue)
    }
) //: ItrCommObserveInt by svernutItr
{


    fun switch(){
        svernutPriv.setValue(!(svernutPriv.getValue() ?: true))
    }
    constructor(startValue:Boolean) : this(startValue, CommonObserveObj<Boolean>())
}