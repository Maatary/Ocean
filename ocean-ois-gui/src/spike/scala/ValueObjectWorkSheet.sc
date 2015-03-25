import javafx.scene.control.TreeItem


val item = new TreeItem[String]("Agents")

item.getValue



def printOnCriteria(x: Int, f: Int => Boolean) = {

  if (f(x) == true) println(x)
}

printOnCriteria(2, e => e % 2 == 0)
printOnCriteria(3, e => e % 2 == 0)

def f (e: Int) = e % 2 == 0

printOnCriteria(4, f)



