package edu.ucsd.ontologymapper;

import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.Node;

import edu.ucsd.dao.OntologyDao;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.NeTags;
import edu.ucsd.model.OwlFragment;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToOntologyClass;

public class OrganizationWordsToOrganizationClassMapper implements
		WordsToOntologyClassMapper {
	
	@Inject
	private SentenceDao sentenceDao;
	
	@Inject
	private OntologyDao ontologyDao;
	
	@Override
	public void map() {
		Node organizationOntologyNode = ontologyDao.getOntologyEntityWithFragment(OwlFragment.ORGANIZATION.getFragmentName());
		List<Word> orgWords = sentenceDao.getWordsWithNeTag(NeTags.ORGANIZATION.name());
		for(Word orgWord : orgWords) {
			ontologyDao.saveWordToOntologyAssociation(new WordToOntologyClass(orgWord, organizationOntologyNode));
		}
	}

}
