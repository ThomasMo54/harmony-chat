import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Platform, StyleSheet, View, ViewStyle } from 'react-native';
import { COLORS } from '@/constants/colors.ts';
import { ReactNode } from 'react';

type HeaderProps = {
  children: ReactNode;
  style?: ViewStyle;
};

function Header({ children, style }: HeaderProps) {
  const insets = useSafeAreaInsets();

  return (
    <View style={[styles.header, { paddingTop: insets.top + 12 }, style]}>
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  header: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    gap: 16,
    width: '100%',
    paddingBottom: 12,
    paddingHorizontal: 16,
    backgroundColor: COLORS.bg,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
    ...Platform.select({
      ios: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.08,
        shadowRadius: 4,
      },
      android: {
        elevation: 4,
      },
    }),
  },
});

export default Header;
