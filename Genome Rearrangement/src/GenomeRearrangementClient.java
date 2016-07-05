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
		
		Integer[] testSequence = {0,1,5,6,7,2,3,4,8,9};
		//4,3,7,6,5,8,0,1,2,9
		//0,1,2,7,6,5,8,4,3, 9
		//0,8,2,7,6,5,1,4,3,9
		//8,7,6,5,4,3,2,1,0, 9
		List<Integer> testGenomicSequence = Arrays.asList(testSequence);
		System.out.println("Number of flips is " +GenomeRearrangement.getNumberOfFlipsToRevertToIdentitySequence(testGenomicSequence));
		
	}
}
