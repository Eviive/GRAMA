
"""
	This class allows us to represent a path between two places

	category  : The type of road
	distance  : The path distance
	arrival   : The pointer to the destination place
	departure : The pointer to the start place
	next      : The pointer to another possible path
"""
class Link:
	def __init__(self, category, distance, departure, arrival, next):
		self.category = category
		self.distance = distance
		self.departure = departure
		self.arrival = arrival
		self.next = next

	"""
		Method to check the equality or the opposite of two links
	"""
	def __eq__(self, other):
		if isinstance(other, Link):
			# Checks the specifications of the road
			specifications = self.category == other.category and self.distance == other.distance

			# Check the place of departure and arrival
			destination = (self.arrival == other.arrival and self.departure == other.departure) or (self.arrival == other.departure and self.arrival == other.departure)

			return specifications and destination 

		return False

"""
	This class allows us to represent a place
	
	category : The type of place
	name     : The place name
	neighbor : A list of paths from this place
"""
class Node:
	def __init__(self, category, name):
		self.category = category
		self.name = name
		self.neighbor = None

	"""
		Method to insert a new neighbor at this location
	"""
	def insertNeighbor(self, category, distance, arrival):
		self.neighbor = Link(category, distance, self, arrival, self.neighbor)

"""
	This class allows us to represent an entire graph

	nodes : A dictionary linking the name of a place with its object
	path  : The path to the file containing the graph data
"""
class Graph:
	def __init__(self,path):
		self.nodes = {}
		self.path = path

	"""
		A method to extract graph data from a file
	"""
	def open(self):

		# First opening to determine all available nodes
		with open(self.path, 'r') as file:
			for row in file.readlines():
				# Extraction of the node's data (1st value before the "|") without carriage return
				node = row.rstrip('\n').split('|')[0]

				# Associating the data to variables
				category, name = node.split(':')

				# Adds to the dictionary a node associated with its name
				self.nodes[name] = Node(category, name)

		# Second opening to determine the neighbors of each place
		with open(self.path, 'r') as file:
			for row in file.readlines():
				# Separation of the line into two parts (node | neighbors) without carriage return
				name, neighbors = row.rstrip('\n').split('|',1)

				# Extraction of the node's name
				name = name.split(':')[1]

				# Loop that runs through all the neighbors
				for neighbor in neighbors.split('|'):
					# Extraction and association of neighbor data
					category, distance, destination = neighbor.split(':')

					# Insertion of the neighbor in the correspondent dictionary node
					self.nodes[name].insertNeighbor(category,distance,self.nodes[destination]) 

	"""
		A method to display all the information of the graph 
	"""	
	def __str__(self):
		size = max([len(e) for e in self.nodes]) + 2
		pattern = "{:" + str(size) + "}"
		text = ""
		if len(self.nodes)>0:
			for node in self.nodes.values():
				text += f"[{node.category}] {pattern.format(node.name)} => "
				neighbor = node.neighbor
				while(neighbor != None):
					text += f" {neighbor.category} | {neighbor.distance:>3} km | {pattern.format(neighbor.arrival.name)}"
					neighbor = neighbor.next
					if neighbor != None:
						text += ' -> '
				text += '\n'
		else:
			text = "No data"	
		return text	

myGraph = Graph("sample.csv")
myGraph.open()
print(myGraph)