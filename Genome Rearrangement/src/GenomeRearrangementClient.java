import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GenomeRearrangementClient {
	
	public static final int SEQUENCE_LENGTH = 10000;
	public static final int NUMBER_OF_ITERATIONS = 1000;

	public static void main(String[] args) {
		
		GenomeRearrangementClient genomeRearrangementClient = new GenomeRearrangementClient();
		genomeRearrangementClient.findIterationsRequired();
	}
	
	public void findIterationsRequired() {
		
		Random randomNumberGenerator = new Random();
		List<Integer> flipsToGenerateSequence = new ArrayList<Integer>(), flipsToRevertToIdentity = new ArrayList<Integer>();
		
		//Save the counts
		for (int counter = 0; counter < NUMBER_OF_ITERATIONS; ++counter) {
			GenomeRearrangement genomeRearrangement = new GenomeRearrangement(SEQUENCE_LENGTH, randomNumberGenerator);
			flipsToGenerateSequence.add(genomeRearrangement.getNumberOfFlipsToGenerateGenomicSequence());
			flipsToRevertToIdentity.add(genomeRearrangement.getnumberOfFlipsToRevertToIdentity());
		}
		
		//Show the counts
		System.out.println("Number of flips to generate sequence: " + flipsToGenerateSequence);
		System.out.println("Number of flips to revert to identity:  " + flipsToRevertToIdentity);
		
	}
}
