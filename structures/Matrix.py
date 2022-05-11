
"""
	This class allows us to represent a path between two places

	category : The type of road
	distance : The path distance
"""
class Link:
	def __init__(self,category, distance):
		self.category = category
		self.distance = distance

"""
	This class allows us to represent a place
	
	category : The type of place
	name     : The place name
"""
class Node:
	def __init__(self,category, name):
		self.category = category
		self.name = name

	"""
		Method to check the equality of two nodes
	"""
	def __eq__(self, other):
		if isinstance(other, Node):
			return self.name == other.name
		else:
			return False


"""
	This class allows us to represent an entire graph
	
	matrix : Two-dimensional table linking two nodes
	node   : Table containing all the graph's nodes
	path   : The path to the file containing the graph data
"""
class Graph:
	def __init__(self, path):
		self.matrix = None
		self.node = None
		self.path = path

	"""
		A method to extract graph data from a file
	"""
	def open(self):

		# First opening to determine the number of nodes
		with open(self.path, 'r') as file:
			counter = 0
			for row in file.readlines():
				counter += 1

		# Creation of the matrix with the right size
		self.matrix = [[None for j in range(counter)] for i in range(counter)]
		# Creation of the table of nodes
		self.node = [None for j in range(counter)] 

		# Second opening to determine all available nodes
		with open(self.path, 'r') as file:
			for index,row in enumerate(file.readlines()):

				# Extraction of the node's data (1st value before the "|") without carriage return
				row = row.rstrip('\n').split('|')[0]

				# Add node data to the table
				self.node[index] = Node(*(row.split(':')))

		# last opening to add neighbors of each nodes
		with open(self.path, 'r') as file:
			for indiceX, row in enumerate(file.readlines()):

				#  Loop that runs to recoveries of all neighbors without a return carriage
				for neighbor in row.rstrip('\n').split('|')[1:]:

					# Extraction and association of the data on neighbors
					category, distance, name = neighbor.split(':')

					# Search in the table of nodes where the path goes
					indiceY = self.node.index(Node(category,name))

					# Adding the path in the matrix
					self.matrix[indiceX][indiceY] = Link(category,distance)
					
	"""
		A method to display all the information of the graph 
	"""	
	def __str__(self):
		size = max([len(i.name) for i in self.node])+5
		pattern = "{:>" + str(size) + "} | "

		text = " "
		text += pattern.format("")
		for element in self.node:
			text += pattern.format(element.name + " [" + element.category +"]")
		text += '\n'
		for index, element in enumerate(self.matrix):
			text += " " + pattern.format(self.node[index].name + " [" + self.node[index].category + "]")
			for val in element:
				if val != None:
					text += pattern.format(val.distance + " km [" + val.category+ "]")
				else:
					text += pattern.format("- ")
			text += '\n'
		return text

myGraph = Graph("sample.csv")
myGraph.open()
print(myGraph)