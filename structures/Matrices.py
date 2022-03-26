class Arete():
	def __init__(self,categorie, distance):
		self.categorie = categorie
		self.distance = distance

class Noeud():
	def __init__(self,categorie, nom):
		self.categorie = categorie
		self.nom = nom

	def __eq__(self, other):
		return self.nom == other.nom

class Graphe():
	def __init__(self, chemin):
		self.matrice = None
		self.sommet = None
		self.chemin = chemin

	def open(self):
		with open(self.chemin, 'r') as file:
			compteur = 0
			for ligne in file.readlines():
				compteur += 1

		self.matrice = [[None for j in range(compteur)] for i in range(compteur)]
		self.sommet = [None for j in range(compteur)] 

		with open(self.chemin, 'r') as file:
			for indice,ligne in enumerate(file.readlines()):
				ligne = ligne.rstrip('\n').split(':')[0]                                             
				self.sommet[indice] = Noeud(*(ligne.split('/')))

		with open(self.chemin, 'r') as file:
			for indice,ligne in enumerate(file.readlines()):
				for voisin in ligne.rstrip('\n').split(':')[1:]:
					categorie,distance,genre,nom = voisin.split('/')
					index = self.sommet.index(Noeud(categorie,nom))
					self.matrice[indice][index] = Arete(categorie,distance)

	def __str__(self):
		taille = max([len(i.nom) for i in self.sommet])+5
		case = "{:>" + str(taille) + "} | "

		texte = " "
		texte += case.format("")
		for element in self.sommet:
			texte += case.format(element.nom + " [" + element.categorie +"]")
		texte += '\n'
		for indice, element in enumerate(self.matrice):
			texte += " " + case.format(self.sommet[indice].nom + " [" + self.sommet[indice].categorie + "]")
			for val in element:
				if val != None:
					texte += case.format(val.distance + " km [" + val.categorie+ "]")
				else:
					texte += case.format("- ")
			texte += '\n'
		return texte

monGraphe = Graphe("Graphe.csv")
monGraphe.open()
print(monGraphe)