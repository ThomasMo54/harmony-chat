import { SafeAreaView } from 'react-native-safe-area-context';
import {
  FlatList,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';
import { COLORS } from '@/constants/colors.ts';
import { findContacts } from '@/api/contactApi.ts';
import usePaginatedList from '@/hooks/usePaginatedList.ts';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { ContactStackParamList } from '@/navigation/types.ts';
import { useNavigation } from '@react-navigation/native';
import ContactListRow from '@/screens/Contact/components/ContactListRow.tsx';
import Header from '@/components/Header.tsx';
import { useTranslation } from 'react-i18next';

type ContactListScreenNavigationProp = NativeStackNavigationProp<
  ContactStackParamList,
  'ContactList'
>;

const contactsPerPage = 20;

function ContactListScreen() {
  const { items: contacts, loadMore: loadMoreContacts } = usePaginatedList(
    page => findContacts(page, contactsPerPage),
  );
  const { t } = useTranslation();
  const navigation = useNavigation<ContactListScreenNavigationProp>();

  return (
    <SafeAreaView style={styles.safe} edges={['bottom']}>
      <StatusBar barStyle="dark-content" />
      <Header style={styles.header}>
        <Text style={styles.title}>{t('contact.list.title')}</Text>
        <TouchableOpacity
          onPress={() => navigation.navigate('ContactRequestList')}
        >
          <AppIcon name="user-plus" size={20} color={COLORS.primary} />
        </TouchableOpacity>
      </Header>
      <FlatList
        data={contacts}
        keyExtractor={item => item.userId}
        renderItem={({ item }) => <ContactListRow contact={item} />}
        onEndReached={() => loadMoreContacts()}
        ItemSeparatorComponent={() => <View style={styles.separator} />}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: COLORS.bg,
  },
  header: {
    justifyContent: 'space-between',
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  addContactButton: {
    alignSelf: 'flex-end',
  },
  disconnectText: {
    color: COLORS.error,
    fontWeight: '600',
  },
  separator: {
    height: 1,
    backgroundColor: COLORS.border,
  },
});

export default ContactListScreen;
