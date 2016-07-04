import java.util.Arrays;
import java.util.List;


public class GenomeRearrangementClient {

	public static void main(String[] args) {
		
		GenomeRearrangementClient genomeRearrangementClient = new GenomeRearrangementClient();
		genomeRearrangementClient.restoreToIdentitySequence();
	}
	
	public void restoreToIdentitySequence() {
		
		//GenomeRearrangement genomeRearrangement = new GenomeRearrangement(10);
		//System.out.println(genomeRearrangement.getGenomicSequence());
		//System.out.println("Number of flips was " + genomeRearrangement.getNumberOfFlipsToGenerateGenomicSequence());
		
		Integer[] testSequence = {0,2,1,3,4,5,8,7,6,9};
		List<Integer> testGenomicSequence = Arrays.asList(testSequence);
		GenomeRearrangement.getNumberOfFlipsToRevertToIdentitySequence(testGenomicSequence);
		
	}
}
