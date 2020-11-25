package hu.bme.aut.shoppinglist.model

import hu.bme.aut.shoppinglist.model.DogData

interface DogDataHolder {
    fun getDogData(): DogData?
}
