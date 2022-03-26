class Arete:
	def __init__(self, categorie, distance, ref):
		self.categorie = categorie
		self.distance = distance
		self.ref = ref
		self.suivant = None

class Noeud:
	def __init__(self, categorie, nom):
		self.categorie = categorie
		self.nom = nom
		self.voisin = None
	
	def voisinEstVide(self):
		return self.voisin == None

	def insertVoisin(self, categorie,distance,ref):
		if self.voisinEstVide():
			self.voisin = Arete(categorie,distance,ref)
		else:
			voisin = self.voisin
			while voisin.suivant != None:
				voisin = voisin.suivant
			voisin.suivant = Arete(categorie,distance,ref)

class Graphe:
	def __init__(self,fichier):
		self.noeuds = {}
		self.fichier = fichier
	
	def open(self):
		with open(self.fichier, 'r') as file:
			for ligne in file.readlines():
				ligne = ligne.rstrip('\n').split(':')[0]
				categorie,nom = ligne.split('/')
				self.noeuds[nom] = Noeud(categorie,nom)
			
		with open(self.fichier, 'r') as file:
			for ligne in file.readlines():
				nom, ligne = ligne.rstrip('\n').split(':',1)
				nom = nom.split('/')[1]
				for voisin in ligne.split(':'):
					link, kilometrage, categorie, destination = voisin.split('/')
					self.noeuds[nom].insertVoisin(link,kilometrage,self.noeuds[destination]) 
					Arete(categorie,kilometrage,self.noeuds[destination])

	def __str__(self):
		taille = max([len(i) for i in self.noeuds]) + 2
		case = "{:" + str(taille) + "}"
		texte = ""
		if len(self.noeuds)>0:
			for noeud in self.noeuds.values():
				texte += "[" + noeud.categorie + "] " + case.format(noeud.nom) + " => "
				voisin = noeud.voisin
				while(voisin != None):
					texte += f" {voisin.categorie} | {voisin.distance:>3} km | {case.format(voisin.ref.nom)}"
					voisin = voisin.suivant
					if voisin != None:
						texte += ' -> '
				texte += '\n'
		else:
			texte = "Aucune valeur"
			
		return texte
		

monGraphe = Graphe("Graphe.csv")
monGraphe.open()
print(monGraphe)