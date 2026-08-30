import {
  FlatList,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { COLORS } from '@/constants/colors.ts';
import { SafeAreaView } from 'react-native-safe-area-context';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { ContactStackParamList } from '@/navigation/types.ts';
import { useTranslation } from 'react-i18next';
import { useState } from 'react';
import usePaginatedList from '@/hooks/usePaginatedList.ts';
import {
  acceptContactRequest,
  CONTACT_ERROR_CODES,
  findReceivedContactRequests,
  rejectContactRequest,
  requestContact,
} from '@/api/contactApi.ts';
import axios from 'axios';
import { USER_ERROR_CODES } from '@/api/userApi.ts';
import Toast from 'react-native-toast-message';
import ContactRequestListRow from '@/screens/Contact/ContactRequestList/components/ContactRequestListRow.tsx';
import Header from '@/components/Header.tsx';

type ContactRequestListScreenNavigationProp = NativeStackNavigationProp<
  ContactStackParamList,
  'ContactRequestList'
>;

const contactRequestsPerPage = 20;

function ContactRequestListScreen() {
  const {
    items: contactRequests,
    loadMore: loadMoreContactRequests,
    removeItem: removeContactRequest,
  } = usePaginatedList(page =>
    findReceivedContactRequests(page, contactRequestsPerPage),
  );
  const { t } = useTranslation();
  const [userSearch, setUserSearch] = useState('');
  const [userSearchError, setUserSearchError] = useState('');
  const navigation = useNavigation<ContactRequestListScreenNavigationProp>();

  async function handleSendRequest() {
    setUserSearchError('');

    try {
      await requestContact({ requestedName: userSearch });
      Toast.show({
        type: 'success',
        text1: t('contact.request.requestSent', { user: userSearch }),
      });
      setUserSearch('');
    } catch (err) {
      handleSearchError(err);
    }
  }

  function handleSearchError(err: unknown) {
    if (!axios.isAxiosError(err)) {
      setUserSearchError(t('common.genericError'));
      return;
    }

    if (!err.response) {
      setUserSearchError(t('common.networkError'));
      return;
    }

    const { status, data } = err.response;

    if (status === 429) {
      setUserSearchError(t('common.rateLimited'));
      return;
    }

    switch (data?.code) {
      case USER_ERROR_CODES.NOT_FOUND: {
        setUserSearchError(t('contact.request.errors.userNotFound'));
        break;
      }
      case CONTACT_ERROR_CODES.REQUEST_TO_SELF: {
        setUserSearchError(t('contact.request.errors.alreadySent'));
        break;
      }
      case CONTACT_ERROR_CODES.ALREADY_IN_CONTACT: {
        setUserSearchError(t('contact.request.errors.alreadyInContacts'));
        break;
      }
      case CONTACT_ERROR_CODES.REQUEST_ALREADY_SENT: {
        setUserSearchError(t('contact.request.errors.requestAlreadySent'));
        break;
      }
      default:
        setUserSearchError(t('contact.request.errors.generic'));
    }
  }

  async function handleAcceptRequest(userId: string) {
    await acceptContactRequest({ requesterId: userId });
    removeContactRequest(request => request.requesterId === userId);
  }

  async function handleRejectRequest(userId: string) {
    await rejectContactRequest({ requesterId: userId });
    removeContactRequest(request => request.requesterId === userId);
  }

  return (
    <SafeAreaView style={styles.safe} edges={['bottom']}>
      <StatusBar barStyle="dark-content" />
      <Header>
        <TouchableOpacity onPress={() => navigation.push('ContactList')}>
          <AppIcon name="arrow-left" size={20} color={COLORS.primary} />
        </TouchableOpacity>
        <Text style={styles.title}>{t('contact.request.title')}</Text>
      </Header>

      <View style={styles.inputWrapper}>
        <AppIcon name={'magnifying-glass'} size={20} />
        <TextInput
          style={styles.userSearchInput}
          value={userSearch}
          onChangeText={setUserSearch}
          onSubmitEditing={handleSendRequest}
          autoCapitalize="none"
          placeholder={t('contact.request.searchPlaceholder')}
          placeholderTextColor="#8A8A8A"
        />
      </View>
      {userSearchError && (
        <Text style={styles.errorText}>{userSearchError}</Text>
      )}

      <View style={styles.separator} />

      <FlatList
        data={contactRequests}
        keyExtractor={item => item.requesterId}
        renderItem={({ item }) => (
          <ContactRequestListRow
            contact={{
              userId: item.requesterId,
              userName: item.requesterName,
              date: item.createdAt,
            }}
            onAccept={handleAcceptRequest}
            onReject={handleRejectRequest}
          />
        )}
        onEndReached={() => loadMoreContactRequests()}
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
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  inputWrapper: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.bg,
    borderWidth: 1,
    borderColor: COLORS.border,
    borderRadius: 12,
    marginTop: 12,
    marginHorizontal: 8,
    paddingHorizontal: 16,
    height: 52,
  },
  userSearchInput: {
    flex: 1,
    fontSize: 16,
    color: COLORS.text,
    marginLeft: 8,
  },
  errorText: {
    color: COLORS.error,
    fontSize: 13,
    fontWeight: '600',
    marginTop: 12,
    marginHorizontal: 8,
  },
  separator: {
    height: 1,
    backgroundColor: COLORS.border,
    marginTop: 12,
  },
});

export default ContactRequestListScreen;
