import { ContactSummaryDto } from '@/api/contactApi.ts';
import { StyleSheet, TouchableOpacity, View } from 'react-native';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';
import { COLORS } from '@/constants/colors.ts';
import ContactListRow from '@/screens/Contact/components/ContactListRow.tsx';

type ContactRequestListRowProps = {
  contact: ContactSummaryDto;
  onAccept: (userId: string) => void;
  onReject: (userId: string) => void;
};

function ContactRequestListRow({
  contact,
  onAccept,
  onReject,
}: ContactRequestListRowProps) {
  return (
    <ContactListRow contact={contact}>
      <View style={styles.buttonsContainer}>
        <TouchableOpacity onPress={() => onAccept(contact.userId)}>
          <AppIcon name={'check'} color={COLORS.green} />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => onReject(contact.userId)}>
          <AppIcon name={'xmark'} color={COLORS.error} />
        </TouchableOpacity>
      </View>
    </ContactListRow>
  );
}

const styles = StyleSheet.create({
  buttonsContainer: {
    marginRight: 8,
    flexDirection: 'row',
    justifyContent: 'flex-end',
    gap: 24,
  },
});

export default ContactRequestListRow;
