package cshcyberhawks.swolib.autonomous

import cshcyberhawks.swolib.autonomous.commands.GoToFieldElements
import cshcyberhawks.swolib.math.Vector2
import kotlin.math.abs

class Field(
    val swerveAuto: SwerveAuto,
    var elements: ArrayList<FieldElement>,
    val deadzone: Double
) {
    fun addElement(element: FieldElement) {
        if (!contains(element)) elements.add(element)
        else throw IllegalArgumentException("Element already in field")
    }

    fun contains(element: FieldElement): Boolean {
        return elements.contains(element)
    }

    fun getElement(name: String): FieldElement {
        for (i: FieldElement in elements) {
            if (name == i.name) return i
        }
        throw IllegalArgumentException("Element not in field")
    }

    fun removeElement(name: String): FieldElement {
        var ind: Int = -1
        for (i in elements.indices) {
            if (name == elements[i].name) {
                ind = i
                break
            }
        }
        if (ind != -1) return elements.removeAt(ind)
        throw IllegalArgumentException("Element not in field")
    }

    fun getElementAtPos(pos: Vector2): List<FieldElement> {
        var list: ArrayList<FieldElement> = ArrayList()
        for (i: FieldElement in elements) {
            if ((abs(pos.x - i.position.x) < deadzone) and (abs(pos.y - i.position.y) < deadzone))
                list.add(i)
        }
        return list.toList()
    }

    fun goToElementsCommand(vararg names: String): GoToFieldElements {
        var list: ArrayList<FieldElement> = ArrayList()
        for (i in names) {
            list.add(getElement(i))
        }
        return GoToFieldElements(swerveAuto, list.toList())
    }

    fun goToElementCommand(name: String): GoToFieldElements {
        return GoToFieldElements(swerveAuto, listOf(getElement(name)))
    }
}
