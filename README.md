# XMLStreaming
Parsing xml files using input streams
###XmlParser has processXML(XmlInputStream xmlis, int depth) method, which takes in a FileInputStream wrapped around with XmlInputStream. The Depth is about the level of parsing.
#Depth of parsing : Level-0 and Level-1
<p> 
level-1 : there is root element and all the target objects are the children of the root element. In the example below the root is class and the objects are student.
<?xml version = "1.0"?>
<class>
    <student rollno = "393">
        <firstname>dinkar</firstname>
        <lastname>kad</lastname>
        <nickname>dinkar</nickname>
        <marks>85</marks>
    </student>

    <student rollno = "493">
        <firstname>Vaneet</firstname>
        <lastname>Gupta</lastname>
        <nickname>vinni</nickname>
        <marks>95</marks>
    </student>

    <student rollno = "593">
        <firstname>jasvir</firstname>
        <lastname>singn</lastname>
        <nickname>jazz</nickname>
        <marks>90</marks>
    </student>
</class>

level-0 : there is no root element and the first element is the record to be parsed.
<student rollno = "593">
    <firstname>jasvir</firstname>
    <lastname>singh</lastname>
    <nickname>jazz</nickname>
    <marks>90</marks>
</student>
</p>
