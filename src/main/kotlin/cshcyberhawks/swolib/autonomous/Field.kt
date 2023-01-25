package frc.robot.subsystems

import cshcyberhawks.swolib.autonomous.FieldElement
import  cshcyberhawks.swolib.math.Vector2
import kotlin.math.abs

class Field(var elements: ArrayList<FieldElement>, val deadzone: Double) {
    fun addElement(element: FieldElement) {
        if(!contains(element))
            elements.add(element)
        else
            throw IllegalArgumentException("Element already in field")
    }

    fun contains(element: FieldElement): Boolean {
        return elements.contains(element)
    }

    fun getElement(name: String): FieldElement {
        for(i : FieldElement in elements) {
            if(name == i.name)
                return i
        }
        throw IllegalArgumentException("Element not in field")
    }

    fun removeElement(name: String): FieldElement {
        var ind: Int = -1
        for(i in elements.indices) {
            if(name == elements[i].name) {
                ind = i
                break
            }
        }
        if(ind != -1)
            return elements.removeAt(ind)
        throw IllegalArgumentException("Element not in field")
    }

    fun getElementAtPos(pos: Vector2): List<FieldElement> {
        var list: ArrayList<FieldElement> = ArrayList()
        for(i : FieldElement in elements) {
            if((abs(pos.x - i.position.x) < deadzone) and (abs(pos.y - i.position.y) < deadzone))
                list.add(i)
        }
        return list.toList()
    }
}