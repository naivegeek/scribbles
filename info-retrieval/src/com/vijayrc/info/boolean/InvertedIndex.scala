package com.vijayrc.info.boolean

import scala.io.Source
import org.scalatest.FunSuite
import scala.collection.mutable
import scala.collection.immutable.TreeMap

/**
 * reads documents then saves map of terms->posting-set(posting(file,occurrences))
 */
class InvertedIndex {
  var treeMap = TreeMap[String,PostingSet]()

  def processDoc(filePath:String, fileId:String){
    val lines: Iterator[String] = Source.fromFile(filePath).getLines()
    lines.foreach(line => {line.split("\\s").foreach(token => processToken(token.replaceAll("[.|,|:|;]",""), fileId))})
  }
  def processToken(token: String, fileId:String){
    if(!treeMap.contains(token)) treeMap += (token -> new PostingSet())
    treeMap(token).add(fileId)
  }
  def print(){
    var str = ""
    treeMap.keys.foreach(term => {str+=term+"["+ treeMap(term).toString +"]\n"})
    println(str)
  }
  def findDocsFor(ands:List[String]):List[String]={
     val foundPostings = mutable.MutableList[mutable.SortedSet[Posting]]()

     ands
  }
  private def intersect(set1:Set[Posting],set2:Set[Posting]):Set[Posting] = {
    if (set1.size >= set2.size) set1.intersect(set2)
    else set2.intersect(set1)
  }
}

/**
 * api with fileId and occurrence count for a given term
 */
class Posting(val fileId:String) extends Ordered[Posting]{
  var occurrences = 1
  def increment(){occurrences+=1}
  override def equals(obj: scala.Any): Boolean = fileId.equals(obj.asInstanceOf[Posting].fileId)
  override def toString: String = fileId+","+occurrences
  override def compare(that: Posting): Int = fileId.compareTo(that.fileId)
}

/**
 * set of all postings for a given term
 */
class PostingSet extends Ordered[PostingSet]{
  val set = mutable.SortedSet[Posting]()

  def add(fileId:String){
    val byFile = set.filter(p => p.fileId.equals(fileId))
    if(byFile.isEmpty) set.+=(new Posting(fileId))
    else byFile.foreach(p => p.increment())
  }
  def totalTermOccurrence:Int = {
    var count = 0
    set.foreach(p=>count+=p.occurrences)
    count
  }
  override def compare(that: PostingSet): Int = this.set.size.compare(that.set.size)
  override def toString: String = {var str=""; set.foreach(p => str += p.toString+"|");str}
}

/**
 *  test it out
 */
class InvertedIndexTest extends FunSuite{
  val index = new InvertedIndex

  test("should make a map"){
    index.processDoc(getClass.getResource("/boolean/file1.txt").getFile,"f1")
    index.processDoc(getClass.getResource("/boolean/file2.txt").getFile,"f2")
    index.print()
    index.findDocsFor(List("South","episode"))
  }
}
